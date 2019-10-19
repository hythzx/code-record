import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { ISubmitRecord, SubmitRecord } from 'app/shared/model/submit-record.model';
import { SubmitRecordService } from './submit-record.service';
import { IDeveloper } from 'app/shared/model/developer.model';
import { DeveloperService } from 'app/entities/developer/developer.service';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project/project.service';

@Component({
  selector: 'jhi-submit-record-update',
  templateUrl: './submit-record-update.component.html'
})
export class SubmitRecordUpdateComponent implements OnInit {
  isSaving: boolean;

  developers: IDeveloper[];

  projects: IProject[];
  recordDateDp: any;

  editForm = this.fb.group({
    id: [],
    recordDate: [null, [Validators.required]],
    cnt: [null, [Validators.required]],
    developerId: [],
    projectId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected submitRecordService: SubmitRecordService,
    protected developerService: DeveloperService,
    protected projectService: ProjectService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ submitRecord }) => {
      this.updateForm(submitRecord);
    });
    this.developerService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IDeveloper[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDeveloper[]>) => response.body)
      )
      .subscribe((res: IDeveloper[]) => (this.developers = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.projectService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProject[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProject[]>) => response.body)
      )
      .subscribe((res: IProject[]) => (this.projects = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(submitRecord: ISubmitRecord) {
    this.editForm.patchValue({
      id: submitRecord.id,
      recordDate: submitRecord.recordDate,
      cnt: submitRecord.cnt,
      developerId: submitRecord.developerId,
      projectId: submitRecord.projectId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const submitRecord = this.createFromForm();
    if (submitRecord.id !== undefined) {
      this.subscribeToSaveResponse(this.submitRecordService.update(submitRecord));
    } else {
      this.subscribeToSaveResponse(this.submitRecordService.create(submitRecord));
    }
  }

  private createFromForm(): ISubmitRecord {
    return {
      ...new SubmitRecord(),
      id: this.editForm.get(['id']).value,
      recordDate: this.editForm.get(['recordDate']).value,
      cnt: this.editForm.get(['cnt']).value,
      developerId: this.editForm.get(['developerId']).value,
      projectId: this.editForm.get(['projectId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubmitRecord>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackDeveloperById(index: number, item: IDeveloper) {
    return item.id;
  }

  trackProjectById(index: number, item: IProject) {
    return item.id;
  }
}
