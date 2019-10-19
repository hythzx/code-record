import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'project',
        loadChildren: () => import('./project/project.module').then(m => m.CodeRecordApplicationProjectModule)
      },
      {
        path: 'developer',
        loadChildren: () => import('./developer/developer.module').then(m => m.CodeRecordApplicationDeveloperModule)
      },
      {
        path: 'submit-record',
        loadChildren: () => import('./submit-record/submit-record.module').then(m => m.CodeRecordApplicationSubmitRecordModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class CodeRecordApplicationEntityModule {}
