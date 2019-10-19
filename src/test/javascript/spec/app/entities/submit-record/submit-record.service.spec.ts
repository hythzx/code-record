import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SubmitRecordService } from 'app/entities/submit-record/submit-record.service';
import { ISubmitRecord, SubmitRecord } from 'app/shared/model/submit-record.model';

describe('Service Tests', () => {
  describe('SubmitRecord Service', () => {
    let injector: TestBed;
    let service: SubmitRecordService;
    let httpMock: HttpTestingController;
    let elemDefault: ISubmitRecord;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(SubmitRecordService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new SubmitRecord(0, currentDate, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            recordDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a SubmitRecord', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            recordDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            recordDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new SubmitRecord(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a SubmitRecord', () => {
        const returnedFromService = Object.assign(
          {
            recordDate: currentDate.format(DATE_FORMAT),
            cnt: 1
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            recordDate: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of SubmitRecord', () => {
        const returnedFromService = Object.assign(
          {
            recordDate: currentDate.format(DATE_FORMAT),
            cnt: 1
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            recordDate: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a SubmitRecord', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
