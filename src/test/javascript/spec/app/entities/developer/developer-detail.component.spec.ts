import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CodeRecordApplicationTestModule } from '../../../test.module';
import { DeveloperDetailComponent } from 'app/entities/developer/developer-detail.component';
import { Developer } from 'app/shared/model/developer.model';

describe('Component Tests', () => {
  describe('Developer Management Detail Component', () => {
    let comp: DeveloperDetailComponent;
    let fixture: ComponentFixture<DeveloperDetailComponent>;
    const route = ({ data: of({ developer: new Developer(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CodeRecordApplicationTestModule],
        declarations: [DeveloperDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DeveloperDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DeveloperDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.developer).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
