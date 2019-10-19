import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CodeRecordApplicationTestModule } from '../../../test.module';
import { SubmitRecordDetailComponent } from 'app/entities/submit-record/submit-record-detail.component';
import { SubmitRecord } from 'app/shared/model/submit-record.model';

describe('Component Tests', () => {
  describe('SubmitRecord Management Detail Component', () => {
    let comp: SubmitRecordDetailComponent;
    let fixture: ComponentFixture<SubmitRecordDetailComponent>;
    const route = ({ data: of({ submitRecord: new SubmitRecord(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CodeRecordApplicationTestModule],
        declarations: [SubmitRecordDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SubmitRecordDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SubmitRecordDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.submitRecord).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
