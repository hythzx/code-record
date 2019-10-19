import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeveloper } from 'app/shared/model/developer.model';

@Component({
  selector: 'jhi-developer-detail',
  templateUrl: './developer-detail.component.html'
})
export class DeveloperDetailComponent implements OnInit {
  developer: IDeveloper;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ developer }) => {
      this.developer = developer;
    });
  }

  previousState() {
    window.history.back();
  }
}
