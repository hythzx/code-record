import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISubmitRecord } from 'app/shared/model/submit-record.model';

@Component({
  selector: 'jhi-submit-record-detail',
  templateUrl: './submit-record-detail.component.html'
})
export class SubmitRecordDetailComponent implements OnInit {
  submitRecord: ISubmitRecord;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ submitRecord }) => {
      this.submitRecord = submitRecord;
    });
  }

  previousState() {
    window.history.back();
  }
}
