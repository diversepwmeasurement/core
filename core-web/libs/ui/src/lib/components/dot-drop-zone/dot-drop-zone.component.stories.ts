import { action } from '@storybook/addon-actions';
import { moduleMetadata, Story, Meta } from '@storybook/angular';

import { CommonModule } from '@angular/common';

import { DotDropZoneComponent } from './dot-drop-zone.component';

export default {
    title: 'Library/ui/Components/DropZone',
    component: DotDropZoneComponent,
    decorators: [
        moduleMetadata({
            imports: [CommonModule]
        })
    ],
    parameters: {
        // https://storybook.js.org/docs/6.5/angular/essentials/actions#action-event-handlers
        actions: {
            // detect if the component is emitting the correct HTML events
            handles: ['fileDrop', 'fileDragEnter', 'fileDragLeave']
        }
    }
} as Meta<DotDropZoneComponent>;

const Template: Story<DotDropZoneComponent> = (args: DotDropZoneComponent) => ({
    props: {
        ...args,
        // https://storybook.js.org/docs/6.5/angular/essentials/actions#action-args
        fileDropped: action('fileDropped'),
        fileDragEnter: action('fileDragEnter'),
        fileDragLeave: action('fileDragLeave')
    },
    styles: [
        `
        .content {
            width: 100%;
            height: 200px;
            background: #f8f9fa;
            display:flex;
            flex-direction: column;
            gap: 1rem;
            justify-content:center;
            align-items:center;
            border: 1px dashed #ced4da;
            border-radius: 5px;
        }
    `
    ],
    template: `
        <dot-drop-zone
            [accept]="accept"
            [maxFileSize]="maxFileSize"
            (fileDropped)="fileDropped($event)"
            (fileDragEnter)="fileDragEnter($event)"
            (fileDragLeave)="fileDragLeave($event)"
        >
            <div class="content">
                Drop files here.

                <div *ngIf="accept.length">
                    <strong>Allowed Type:</strong> {{ accept }}
                </div>

                <div *ngIf="maxFileSize">
                    <strong>Max File Size:</strong> {{ maxFileSize }}
                </div>
            </div>
        </dot-drop-zone>
    `
});

export const Base = Template.bind({});

Base.args = {
    accept: [],
    maxFileSize: 1000000
};