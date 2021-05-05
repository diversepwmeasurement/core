package com.dotcms.cache.transport.postgres;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import com.dotcms.cache.transport.postgres.CachePubSubTopic.CacheEventType;
import com.dotcms.cluster.bean.Server;
import com.dotcms.dotpubsub.DotPubSubEvent;
import com.dotcms.dotpubsub.DotPubSubProvider;
import com.dotcms.dotpubsub.DotPubSubProviderLocator;
import com.dotcms.enterprise.cluster.ClusterFactory;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.cache.transport.CacheTransport;
import com.dotmarketing.business.cache.transport.CacheTransportException;
import com.dotmarketing.util.Logger;
import io.vavr.control.Try;
import jersey.repackaged.com.google.common.collect.ImmutableMap;

public class PostgresCacheTransport implements CacheTransport {

    final DotPubSubProvider pubsub;

    final CachePubSubTopic topic;
    final ServerResponseTopic serverResponseTopic;
    final AtomicBoolean initialized = new AtomicBoolean(false);


    @Override
    public boolean requiresAutowiring() {
        return false;
    }



    public PostgresCacheTransport() {
        this.pubsub = DotPubSubProviderLocator.provider.get();
        this.topic = new CachePubSubTopic();
        this.serverResponseTopic = new ServerResponseTopic();
        Logger.debug(this.getClass(), "PostgresCacheTransport");

    }



    @Override
    public void init(final Server localServer) throws CacheTransportException {
        
        Logger.info(this.getClass(), "initing PostgresCacheTransport");
        this.pubsub.start();
        this.pubsub.subscribe(topic);
        this.pubsub.subscribe(serverResponseTopic);

        this.initialized.set(true);

        
        
    }



    @Override
    public void send(final String message) throws CacheTransportException {
        if(!this.initialized.get()) {
            return;
        }

        final DotPubSubEvent event =
                        new DotPubSubEvent.Builder().withTopic(this.topic).withType(CacheEventType.INVAL.name()).withMessage(message).build();

        this.pubsub.publish( event);


    }



    @Override
    public void testCluster() throws CacheTransportException {

        Logger.info(this.getClass(), "Sending PING to cluster ");
        final DotPubSubEvent event = new DotPubSubEvent.Builder()
                        .withType(CachePubSubTopic.CacheEventType.PING.name())
                        .withTopic(this.topic)
                        
                        .build();
        
        this.pubsub.publish( event);
        

        
    
    }


    
    
    


    @Override
    public Map<String, Serializable> validateCacheInCluster(final int maxWaitInMillis)
                    throws CacheTransportException {

        final DotPubSubEvent event = new DotPubSubEvent.Builder()
                        .withType(CachePubSubTopic.CacheEventType.CLUSTER_REQ.name())
                        .withTopic(this.topic)
                        .build();
        
        serverResponseTopic.resetMap();
        
        final int numberOfOtherServers = Try.of(()-> APILocator.getServerAPI().getAliveServers().size()).getOrElse(0);
        
        this.pubsub.publish(event);
        
        final long waitUntil = System.currentTimeMillis() + maxWaitInMillis;
        
        
        while(System.currentTimeMillis() < waitUntil) {

            if(serverResponseTopic.size()>=numberOfOtherServers) {
                return serverResponseTopic.readResponses();
            }
            
            
        }
        
        
        return serverResponseTopic.readResponses();
    }



    @Override
    public void shutdown() throws CacheTransportException {
        Logger.debug(this.getClass(), "shutdown()");
        this.pubsub.stop();
    }



    @Override
    public boolean isInitialized() {
        Logger.debug(this.getClass(), "isInitialized");
        return initialized.get();
    }



    @Override
    public boolean shouldReinit() {

        return !initialized.get();
    }



    @Override
    public CacheTransportInfo getInfo() {
        

        return new CacheTransportInfo(){
            @Override
            public String getClusterName() {
                return ClusterFactory.getClusterId();
            }

            @Override
            public String getAddress() {
                return "n/a";
            }

            @Override
            public int getPort() {
                return -1;
            }


            @Override
            public boolean isOpen() {
                return true;
            }

            @Override
            public int getNumberOfNodes() {
                return Try.of(()-> APILocator.getServerAPI().getAliveServers().size()).getOrElse(-1);
            }


            @Override
            public long getReceivedBytes() {
                return topic.bytesReceived();
            }

            @Override
            public long getReceivedMessages() {
                return topic.messagesReceived();
            }

            @Override
            public long getSentBytes() {
                return topic.bytesSent();
            }

            @Override
            public long getSentMessages() {
                return topic.messagesSent();
            }
        };
    }


}
