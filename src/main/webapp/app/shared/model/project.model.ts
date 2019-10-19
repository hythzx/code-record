export interface IProject {
  id?: number;
  name?: string;
  code?: string;
  desc?: any;
}

export class Project implements IProject {
  constructor(public id?: number, public name?: string, public code?: string, public desc?: any) {}
}
