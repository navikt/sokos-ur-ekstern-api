# Sokos-ur-ekstern-api
Applikasjon som eksponerer ytelser fra ur til andre organisasjoner via maskinporten og til interne applikasjoner via azureAD

# Test
OpenApi er tilgjengelig på: https://sokos-ur-ekstern-api.intern.dev.nav.no/api/v1/docs

Maskinporten token kan hentes ved å kjøre  MaskinportenAccessTokenClient fra devtools mappa.   
Maskinporten miljøvariabler kan hentes ved å kjøre:
```sh
kubectl exec -nokonomi -it $(kubectl get pods -nokonomi | grep sokos-ur-ekstern-api | cut -f1 -d' ') -- env | egrep "^MASKIN"
```