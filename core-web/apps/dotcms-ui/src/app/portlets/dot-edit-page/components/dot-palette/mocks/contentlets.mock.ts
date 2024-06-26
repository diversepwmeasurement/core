import { DEFAULT_VARIANT_ID, DotCMSContentlet } from '@dotcms/dotcms-models';

const IDENTIFIER = '0af1efad-6f3c-480e-bb91-fe786a4b6dfe';
export const VARIANT_ID_MOCK = 'dotexperiment-3759acc113-variant-1';

export const ContentletWithDuplicatedMock: Array<DotCMSContentlet> = [
    {
        hostName: 'demo.dotcms.com',
        modDate: '2023-10-03 17:47:16.198',
        publishDate: '2023-10-03 17:47:16.198',
        title: 'Travel Blog Header [MODIFIED]',
        body: '<h1>Travel Blog MODIFIED</h1>',
        baseType: 'CONTENT',
        inode: 'cf2af7da-6968-48ef-97fa-d638ba7def01',
        archived: false,
        host: '48190c8c-42c4-46af-8d1a-0cd5db894797',
        working: true,
        locked: false,
        stInode: '2a3e91e4-fbbf-4876-8c5b-2233c1739b05',
        contentType: 'webPageContent',
        live: true,
        owner: 'dotcms.org.1',
        identifier: IDENTIFIER,
        languageId: 1,
        url: '/content.5f3ba352-0139-425e-880f-c8bbfafcea7d',
        titleImage: 'TITLE_IMAGE_NOT_FOUND',
        modUserName: 'Admin User',
        hasLiveVersion: true,
        folder: 'SYSTEM_FOLDER',
        hasTitleImage: false,
        sortOrder: 0,
        modUser: 'dotcms.org.1',
        __icon__: 'contentIcon',
        contentTypeIcon: 'wysiwyg',
        variant: VARIANT_ID_MOCK
    },

    {
        hostName: 'demo.dotcms.com',
        modDate: '2020-09-02 16:45:50.663',
        publishDate: '2020-09-02 16:45:50.663',
        title: 'Travel Blog Header [Original]',
        body: '<h1>Travel Blog ORIGINAL</h1>',
        baseType: 'CONTENT',
        inode: '782c7e2c-5c95-41fd-83aa-d3ff8cb143d3',
        archived: false,
        host: '48190c8c-42c4-46af-8d1a-0cd5db894797',
        working: true,
        locked: false,
        stInode: '2a3e91e4-fbbf-4876-8c5b-2233c1739b05',
        contentType: 'webPageContent',
        live: true,
        owner: 'dotcms.org.1',
        identifier: IDENTIFIER,
        languageId: 1,
        url: '/content.5f3ba352-0139-425e-880f-c8bbfafcea7d',
        titleImage: 'TITLE_IMAGE_NOT_FOUND',
        modUserName: 'Admin User',
        hasLiveVersion: true,
        folder: 'SYSTEM_FOLDER',
        hasTitleImage: false,
        sortOrder: 0,
        modUser: 'dotcms.org.1',
        __icon__: 'contentIcon',
        contentTypeIcon: 'wysiwyg',
        variant: DEFAULT_VARIANT_ID
    }
];

export const NotDuplicatedContentletMock: Array<DotCMSContentlet> = [
    {
        hostName: 'demo.dotcms.com',
        modDate: '2020-09-02 16:45:53.832',
        publishDate: '2020-09-02 16:45:53.832',
        title: 'Thank You [ORIGINAL]',
        body: '<h1 style="text-align: center;">Thank You</h1>\n<p>Thank you for your interest in TravelLux, the industry leader in luxury adventure travel. We have received your information and are reviewing it. One of our team members will reach out to you shortly. If you need immediate assistance please call us at 1-800-LUX-TRAV.</p>\n<div class="hr-logo"></div>\n<p></p>',
        baseType: 'CONTENT',
        inode: 'b614f0a1-02fd-4a09-b62c-81e7973eeb40',
        archived: false,
        host: '48190c8c-42c4-46af-8d1a-0cd5db894797',
        working: true,
        locked: false,
        stInode: '2a3e91e4-fbbf-4876-8c5b-2233c1739b05',
        contentType: 'webPageContent',
        live: true,
        owner: 'dotcms.org.1',
        identifier: 'e3988576-cf62-437b-ac93-6237baf519c5',
        languageId: 1,
        url: '/content.c1831446-8eb9-4b15-b7ea-43d6301f51b5',
        titleImage: 'TITLE_IMAGE_NOT_FOUND',
        modUserName: 'Admin User',
        hasLiveVersion: true,
        folder: 'SYSTEM_FOLDER',
        hasTitleImage: false,
        sortOrder: 0,
        modUser: 'dotcms.org.1',
        __icon__: 'contentIcon',
        contentTypeIcon: 'wysiwyg',
        variant: DEFAULT_VARIANT_ID
    }
];

export const NewVariantContentletMock: Array<DotCMSContentlet> = [
    {
        hostName: 'demo.dotcms.com',
        modDate: '2023-10-04 17:14:25.153',
        publishDate: '2023-10-04 17:14:25.153',
        title: 'New Variant Contentlet',
        body: '<p>TEST Rich 2</p>',
        baseType: 'CONTENT',
        inode: 'd1fdadf0-2782-4680-986c-caf63b40a787',
        archived: false,
        host: '48190c8c-42c4-46af-8d1a-0cd5db894797',
        working: true,
        locked: false,
        stInode: '2a3e91e4-fbbf-4876-8c5b-2233c1739b05',
        contentType: 'webPageContent',
        live: true,
        owner: 'dotcms.org.1',
        identifier: '7de976503e17c7f51f6b24433187365c',
        languageId: 1,
        url: '/content.3dd13f81-68a7-4690-ae05-9ce03b830cf2',
        titleImage: 'TITLE_IMAGE_NOT_FOUND',
        modUserName: 'Admin User',
        hasLiveVersion: true,
        folder: 'SYSTEM_FOLDER',
        hasTitleImage: false,
        sortOrder: 0,
        modUser: 'dotcms.org.1',
        __icon__: 'contentIcon',
        contentTypeIcon: 'wysiwyg',
        variant: VARIANT_ID_MOCK
    }
];
