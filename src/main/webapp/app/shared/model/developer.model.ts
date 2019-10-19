export interface IDeveloper {
  id?: number;
  name?: string;
}

export class Developer implements IDeveloper {
  constructor(public id?: number, public name?: string) {}
}
