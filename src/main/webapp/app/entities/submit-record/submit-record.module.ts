import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CodeRecordApplicationSharedModule } from 'app/shared/shared.module';
import { SubmitRecordComponent } from './submit-record.component';
import { SubmitRecordDetailComponent } from './submit-record-detail.component';
import { SubmitRecordUpdateComponent } from './submit-record-update.component';
import { SubmitRecordDeletePopupComponent, SubmitRecordDeleteDialogComponent } from './submit-record-delete-dialog.component';
import { submitRecordRoute, submitRecordPopupRoute } from './submit-record.route';

const ENTITY_STATES = [...submitRecordRoute, ...submitRecordPopupRoute];

@NgModule({
  imports: [CodeRecordApplicationSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    SubmitRecordComponent,
    SubmitRecordDetailComponent,
    SubmitRecordUpdateComponent,
    SubmitRecordDeleteDialogComponent,
    SubmitRecordDeletePopupComponent
  ],
  entryComponents: [SubmitRecordDeleteDialogComponent]
})
export class CodeRecordApplicationSubmitRecordModule {}
