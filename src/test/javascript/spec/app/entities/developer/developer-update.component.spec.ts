import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CodeRecordApplicationTestModule } from '../../../test.module';
import { DeveloperUpdateComponent } from 'app/entities/developer/developer-update.component';
import { DeveloperService } from 'app/entities/developer/developer.service';
import { Developer } from 'app/shared/model/developer.model';

describe('Component Tests', () => {
  describe('Developer Management Update Component', () => {
    let comp: DeveloperUpdateComponent;
    let fixture: ComponentFixture<DeveloperUpdateComponent>;
    let service: DeveloperService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CodeRecordApplicationTestModule],
        declarations: [DeveloperUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DeveloperUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DeveloperUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DeveloperService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Developer(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Developer();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
