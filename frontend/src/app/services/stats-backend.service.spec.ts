import { TestBed } from '@angular/core/testing';

import { StatsBackendService } from './stats-backend.service';

describe('StatsService', () => {
  let service: StatsBackendService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StatsBackendService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
