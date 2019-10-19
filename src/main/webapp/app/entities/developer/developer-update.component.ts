import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IDeveloper, Developer } from 'app/shared/model/developer.model';
import { DeveloperService } from './developer.service';

@Component({
  selector: 'jhi-developer-update',
  templateUrl: './developer-update.component.html'
})
export class DeveloperUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(0), Validators.maxLength(12)]]
  });

  constructor(protected developerService: DeveloperService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ developer }) => {
      this.updateForm(developer);
    });
  }

  updateForm(developer: IDeveloper) {
    this.editForm.patchValue({
      id: developer.id,
      name: developer.name
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const developer = this.createFromForm();
    if (developer.id !== undefined) {
      this.subscribeToSaveResponse(this.developerService.update(developer));
    } else {
      this.subscribeToSaveResponse(this.developerService.create(developer));
    }
  }

  private createFromForm(): IDeveloper {
    return {
      ...new Developer(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeveloper>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
