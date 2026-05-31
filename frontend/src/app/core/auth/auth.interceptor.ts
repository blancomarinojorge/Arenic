import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from './auth.service';

/*every request that goes out passes through this function adding the jwt token if founded in the auth service*/
export const authInterceptor: HttpInterceptorFn = (req, next) => {

  /*In the cases we are dealing with auth requests, we dont apply this interceptor.
  * If we did, we would have an infinite loop trying to refresh the token on
  * 401 responses. Since the server responds with 401 too in case the refresh token
  * has expired or is invalid.
  *
  * */
  if (req.url.includes('/auth/')){
    return next(req);
  }


  const authService = inject(AuthService);
  const token = authService.getAccessToken();

  /* 1. Add the token to the request */
  const authReq = token
    ? req.clone({setHeaders: {Authorization: `Bearer ${token}`}})
    : req;

  /* 2. We call next so the request continues. In case the request throws a 401
  *     response, we intercept it here and try to refresh the token. In the case
  *     the refresh itself fails we propagate the error so the calling component
  *     can manage it.
  *
  *
  *       1. GET /api/users  →  Interceptor adds Bearer oldToken  →  Server
  *       2. Server returns 401 (token expired)
  *       3. catchError catches the 401
  *       4. refreshAccessToken() → POST /auth/refresh with refreshToken
  *       5. Server returns { accessToken: newToken, refreshToken: newRefresh }
  *       6. tap() → storeTokens() saves new tokens to localStorage + signal
  *       7. switchMap() → ignores refresh response
  *       8. Gets newToken from signal
  *       9. Clones original request with Bearer newToken
  *       10. next(retried) → GET /api/users again with new token → Server
  *       11. Server returns 200 with users data
  *       12. Original caller gets the data as if nothing happened
  *
  * */
  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {

      /* If it's not 401 error, we just throw the error so the components handle it normally */
      if (error.status !== 401){
        return throwError(() => error);
      }

      /* If no refresh token available - Logout */
      const refresh$ = authService.refreshAccessToken();
      if (!refresh$){
        authService.logout();
        return throwError(() => error);
      }

      /* Otherwise try to refresh it, then try the original request */
      return refresh$.pipe(
        /* We discard the refresh Observable with switchmap and make the ORIGINAL request again,
        *  returning the observable to the component like nothing happened
        *  */
        switchMap(() => {
          /* In case of succesfull refresh, we add the new jwt token to the request */
          const newToken = authService.getAccessToken();
          const retried = req.clone({
            setHeaders: {Authorization: `Bearer ${newToken}`}
          });
          return next(retried);
        }),
        catchError((refreshError: HttpErrorResponse) => {
          /* In case the refresh attempt fails, we log out the user */
          authService.logout();
          return throwError(() => refreshError);
        })
      )

    })
  );
}
