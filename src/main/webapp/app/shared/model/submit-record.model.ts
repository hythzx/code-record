import { Moment } from 'moment';

export interface ISubmitRecord {
  id?: number;
  recordDate?: Moment;
  cnt?: number;
  developerName?: string;
  developerId?: number;
  projectName?: string;
  projectId?: number;
}

export class SubmitRecord implements ISubmitRecord {
  constructor(
    public id?: number,
    public recordDate?: Moment,
    public cnt?: number,
    public developerName?: string,
    public developerId?: number,
    public projectName?: string,
    public projectId?: number
  ) {}
}
