import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CodeRecordApplicationSharedModule } from 'app/shared/shared.module';
import { DeveloperComponent } from './developer.component';
import { DeveloperDetailComponent } from './developer-detail.component';
import { DeveloperUpdateComponent } from './developer-update.component';
import { DeveloperDeletePopupComponent, DeveloperDeleteDialogComponent } from './developer-delete-dialog.component';
import { developerRoute, developerPopupRoute } from './developer.route';

const ENTITY_STATES = [...developerRoute, ...developerPopupRoute];

@NgModule({
  imports: [CodeRecordApplicationSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DeveloperComponent,
    DeveloperDetailComponent,
    DeveloperUpdateComponent,
    DeveloperDeleteDialogComponent,
    DeveloperDeletePopupComponent
  ],
  entryComponents: [DeveloperDeleteDialogComponent]
})
export class CodeRecordApplicationDeveloperModule {}
