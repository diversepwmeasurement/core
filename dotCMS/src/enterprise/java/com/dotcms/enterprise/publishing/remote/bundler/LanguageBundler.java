/* 
* Licensed to dotCMS LLC under the dotCMS Enterprise License (the
* “Enterprise License”) found below 
* 
* Copyright (c) 2023 dotCMS Inc.
* 
* With regard to the dotCMS Software and this code:
* 
* This software, source code and associated documentation files (the
* "Software")  may only be modified and used if you (and any entity that
* you represent) have:
* 
* 1. Agreed to and are in compliance with, the dotCMS Subscription Terms
* of Service, available at https://www.dotcms.com/terms (the “Enterprise
* Terms”) or have another agreement governing the licensing and use of the
* Software between you and dotCMS. 2. Each dotCMS instance that uses
* enterprise features enabled by the code in this directory is licensed
* under these agreements and has a separate and valid dotCMS Enterprise
* server key issued by dotCMS.
* 
* Subject to these terms, you are free to modify this Software and publish
* patches to the Software if you agree that dotCMS and/or its licensors
* (as applicable) retain all right, title and interest in and to all such
* modifications and/or patches, and all such modifications and/or patches
* may only be used, copied, modified, displayed, distributed, or otherwise
* exploited with a valid dotCMS Enterprise license for the correct number
* of dotCMS instances.  You agree that dotCMS and/or its licensors (as
* applicable) retain all right, title and interest in and to all such
* modifications.  You are not granted any other rights beyond what is
* expressly stated herein.  Subject to the foregoing, it is forbidden to
* copy, merge, publish, distribute, sublicense, and/or sell the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
* OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
* IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
* CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
* TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
* SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
* 
* For all third party components incorporated into the dotCMS Software,
* those components are licensed under the original license provided by the
* owner of the applicable component.
*/

package com.dotcms.enterprise.publishing.remote.bundler;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import com.dotcms.enterprise.LicenseUtil;
import com.dotcms.enterprise.license.LicenseLevel;
import com.dotcms.publisher.business.DotPublisherException;
import com.dotcms.publisher.business.PublishAuditAPI;
import com.dotcms.publisher.business.PublishAuditAPIImpl;
import com.dotcms.publisher.business.PublishAuditHistory;
import com.dotcms.publisher.business.PublishAuditStatus;
import com.dotcms.publisher.pusher.PushPublisherConfig;
import com.dotcms.publisher.pusher.wrapper.LanguageWrapper;
import com.dotcms.publishing.*;
import com.dotcms.publishing.output.BundleOutput;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.languagesmanager.model.Language;
import com.dotmarketing.util.PushPublishLogger;

/**
 * This bundler will take the list of {@link Language} objects that are being
 * pushed and will write them in the file system in the form of an XML file.
 * This information will be part of the bundle that will be pushed to the
 * destination server.
 * 
 * @author Jorge Urdaneta
 * @version 1.0
 * @since Mar 7, 2013
 *
 */
public class LanguageBundler implements IBundler {

	public final static String LANGUAGE_EXTENSION = ".language.xml" ;
	private PushPublisherConfig config;

	@Override
	public String getName() {
		return "Language bundler";
	}

	@Override
	public void setConfig(PublisherConfig pc) {
		config = (PushPublisherConfig) pc;
	}

    @Override
    public void setPublisher(IPublisher publisher) {
    }

	@Override
	public void generate(final BundleOutput output, final BundlerStatus status)
			throws DotBundleException {
	    if(LicenseUtil.getLevel() < LicenseLevel.PROFESSIONAL.level) {
	        throw new RuntimeException("need an enterprise pro license to run this bundler");
	    }
	    PublishAuditAPI publishAuditAPI = PublishAuditAPIImpl.getInstance();
		PublishAuditHistory currentStatusHistory = null;
		try {

			Set<String> languagesIds = config.getIncludedLanguages();
			// Updating the audit table
			if (!config.isDownloading() && languagesIds.size() > 0) {
				currentStatusHistory = publishAuditAPI.getPublishAuditStatus(config.getId()).getStatusPojo();
				if (currentStatusHistory == null) {
					currentStatusHistory = new PublishAuditHistory();
				}
				if (currentStatusHistory.getBundleStart() == null) {
					currentStatusHistory.setBundleStart(new Date());
					PushPublishLogger.log(this.getClass(), "Status Update: Bundling.");
					publishAuditAPI.updatePublishAuditStatus(config.getId(), PublishAuditStatus.Status.BUNDLING,
							currentStatusHistory);
				}
			}
			for (String id : languagesIds) {
				Language language = APILocator.getLanguageAPI().getLanguage(id);
				writeLanguage(output, language);
			}
			// Updating the audit table
			if (currentStatusHistory != null && !config.isDownloading() && languagesIds.size() > 0) {
				currentStatusHistory = publishAuditAPI.getPublishAuditStatus(config.getId()).getStatusPojo();
				currentStatusHistory.setBundleEnd(new Date());
				PushPublishLogger.log(this.getClass(), "Status Update: Bundling.");
				publishAuditAPI.updatePublishAuditStatus(config.getId(), PublishAuditStatus.Status.BUNDLING,
						currentStatusHistory);
			}
		} catch (Exception e) {
			status.addFailure();

			throw new DotBundleException(this.getClass().getName() + " : " + "generate()"
			+ e.getMessage() + ": Unable to pull content", e);
		}

	}

	/**
	 * Writes the properties of a {@link Language} object to the file system, so
	 * that it can be bundled and pushed to the destination server.
	 * 

	 * @param output
	 *            - The root location of the bundle in the file system.
	 * @throws IOException
	 *             An error occurred when writing the rule to the file system.
	 * @throws DotDataException
	 *             An error occurred accessing the database.
	 * @throws DotSecurityException
	 *             The current user does not have the required permissions to
	 *             perform this action.
	 */
	private void writeLanguage(final BundleOutput output, final Language language)
			throws IOException, DotBundleException, DotDataException,
			DotSecurityException, DotPublisherException
	{

		LanguageWrapper wrapper = new LanguageWrapper(language);
		wrapper.setOperation(config.getOperation());

		String uri = language.getLanguageCode() + "_" + language.getCountryCode();
		if(!uri.endsWith(LANGUAGE_EXTENSION)){
			uri.replace(LANGUAGE_EXTENSION, "");
			uri.trim();
			uri += LANGUAGE_EXTENSION;
		}

		String myFileUrl = File.separator
				+"live" + File.separator
				+ APILocator.getHostAPI().findSystemHost().getHostname() +File.separator + uri;

		if (!output.exists(myFileUrl)) {
			try (final OutputStream outputStream = output.addFile(myFileUrl)) {
				BundlerUtil.objectToXML(wrapper, outputStream);
				output.setLastModified(myFileUrl, Calendar.getInstance().getTimeInMillis());
			}
		}
	}

	@Override
	public FileFilter getFileFilter(){
		return new LanguageBundlerFilter();
	}

	/**
	 * A simple file filter that looks for Language data files inside a bundle.
	 * 
	 * @author Jorge Urdaneta
	 * @version 1.0
	 * @since Mar 7, 2013
	 *
	 */
	public class LanguageBundlerFilter implements FileFilter{

		@Override
		public boolean accept(File pathname) {

			return (pathname.isDirectory() || pathname.getName().endsWith(LANGUAGE_EXTENSION));
		}

	}
}
