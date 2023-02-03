import { KeycloakService } from "keycloak-angular";


export function initializeKeycloak(
  keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        url: 'http://localhost:8080/',
        realm:'eshop',
        clientId: 'eshop-ui',
      },
      initOptions: {
        pkceMethod: 'S256',
        redirectUri: 'http://localhost:4200/',
        checkLoginIframe: false
      }
    });
}
