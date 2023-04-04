import { TestBed } from '@angular/core/testing';

import { UserRouteAccessService } from './user-route-access.service';

describe('UserRouteAccessService', () => {
  let service: UserRouteAccessService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserRouteAccessService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
