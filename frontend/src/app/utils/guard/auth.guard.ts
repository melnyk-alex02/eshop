import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { inject } from "@angular/core";
import { UserBackendService } from "../../services/user-backend.service";
import { User } from "../../models/user";
import { catchError, map, Observable, of } from "rxjs";

export const authGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
): Observable<boolean | UrlTree> => {
  const userService = inject(UserBackendService);
  const router = inject(Router);


  return userService.getCurrentUser().pipe(
    map((user: User) => {
      const requiredRoles = route.data?.['roles'];
      if (user && user.roles && user.roles.some((r: string) => requiredRoles.includes(r))) {
        return true;
      } else if (user && user.roles && user.roles.includes('ROLE_USER') && !user.roles.includes('ROLE_ADMIN')) {
        return router.createUrlTree(['/not-found']);
      } else {
        return router.createUrlTree(['/login'], {queryParams: {returnUrl: state.url}});
      }
    }),
    catchError((err) => {
      return of(router.createUrlTree(['/login'], {queryParams: {returnUrl: state.url}}));
    })
  );
};
