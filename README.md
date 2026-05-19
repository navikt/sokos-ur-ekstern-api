# Sokos-ur-ekstern-api
Applikasjon som eksponerer ytelser fra ur til andre organisasjoner via maskinporten og til interne applikasjoner via azureAD

# Test
OpenApi er tilgjengelig på: https://sokos-ur-ekstern-api.intern.dev.nav.no/api/v1/docs

Maskinporten token kan hentes ved å kjøre  MaskinportenAccessTokenClient fra devtools mappa.   
Maskinporten miljøvariabler kan hentes ved å kjøre:
```sh
kubectl exec -nokonomi -it $(kubectl get pods -nokonomi | grep sokos-ur-ekstern-api | cut -f1 -d' ') -- env | egrep "^MASKIN"
```

## Ytelseskoder/typer
| Ytelses-kode | Beskrivelse | Delytelser|
| :--- | :--- | :--- |
|AAP   |Arbeidsavklaringspenger |Arbeidsavklaringspenger              |
|AAP   |Arbeidsavklaringspenger |Attføringspenger                     |
|AAP   |Arbeidsavklaringspenger |Barnetillegg                         |
|AAP   |Arbeidsavklaringspenger |Eøs        |
|AAP   |Arbeidsavklaringspenger |Med sykepengerett ordinær            |
|AAP   |Arbeidsavklaringspenger |Med sykepengerett yrkessk            |
|AAP   |Arbeidsavklaringspenger |Med sykepengerett EØS                |
|AAP   |Arbeidsavklaringspenger |Ordinær    |
|AAP   |Arbeidsavklaringspenger |Tidsbegr. uførestønad ord            |
|AAP   |Arbeidsavklaringspenger |Yrkesskade |
|AFP   |Avtalefestet pensjon    |AFP-tillegg|
|AFP   |Avtalefestet pensjon    |AFP-Barnetillegg                     |
|AFP   |Avtalefestet pensjon    |AFP-Ektefelletillegg                 |
|AFP   |Avtalefestet pensjon    |AFP-Grunnpensjon                     |
|AFP   |Avtalefestet pensjon    |AFP-Kompensasjonstillegg             |
|AFP   |Avtalefestet pensjon    |AFP-Kronetillegg                     |
|AFP   |Avtalefestet pensjon    |AFP-Livsvarig del                    |
|AFP   |Avtalefestet pensjon    |AFP-Minstenivåtillegg                |
|AFP   |Avtalefestet pensjon    |AFP-Særtillegg                       |
|AFP   |Avtalefestet pensjon    |AFP-Tilleggspensjon                  |
|AFP   |Avtalefestet pensjon    |Feilutbetalt AFP                     |
|ALDERSPENSJON  |Alderspensjon  |Alder-Barnetillegg                   |
|ALDERSPENSJON  |Alderspensjon  |Alder-Ektefelletillegg               |
|ALDERSPENSJON  |Alderspensjon  |Alder-Familietillegg                 |
|ALDERSPENSJON  |Alderspensjon  |Alder-Faste utg.(inst)               |
|ALDERSPENSJON  |Alderspensjon  |Alder-Garantipensjon                 |
|ALDERSPENSJON  |Alderspensjon  |Alder-Garantitillegg                 |
|ALDERSPENSJON  |Alderspensjon  |Alder-Garantitillegg(EØS)            |
|ALDERSPENSJON  |Alderspensjon  |Alder-Gjenlevendetillegg             |
|ALDERSPENSJON  |Alderspensjon  |Alder-Grunnpensjon                   |
|ALDERSPENSJON  |Alderspensjon  |Alder-Inntektspensjon                |
|ALDERSPENSJON  |Alderspensjon  |Alder-Minstenivåtillegg              |
|ALDERSPENSJON  |Alderspensjon  |Alder-Pensjonstillegg                |
|ALDERSPENSJON  |Alderspensjon  |Alder-Renter                         |
|ALDERSPENSJON  |Alderspensjon  |Alder-Skjermingstilleggg             |
|ALDERSPENSJON  |Alderspensjon  |Alder-Særtillegg                     |
|ALDERSPENSJON  |Alderspensjon  |Alder-Tilleggspensjon                |
|ALDERSPENSJON  |Alderspensjon  |Alder-Uføretillegg                   |
|ALDERSPENSJON  |Alderspensjon  |Alder-Ventetillegg                   |
|BARNEPENSJON|Barnepensjon      |Barnepensjon                         |
|BARNEPENSJON|Barnepensjon      |Barnepensjon-Grunnpensjon            |
|BARNEPENSJON|Barnepensjon      |Barnepensjon-Særtillegg              |
|BARNEPENSJON|Barnepensjon      |Barnepensjon-Tilleggsp.              |
|BARNEPENSJON|Barnepensjon      |BARNEPENSJON                         |
|DAGPENGER|Dagpenger            |Dagpenger  |
|DAGPENGER|Dagpenger            |Dagpenger ved arbeidsløshet          |
|DAGPENGER|Dagpenger            |Dagpenger, ferietillegg              |
|DAGPENGER|Dagpenger            |Lønnskompensasjon                    |
|ETTERLATTEPENSJON |Etterlattepensjon                     |Familiepleier -Grunnpensj            |
|ETTERLATTEPENSJON |Etterlattepensjon                     |Familiepleier- Renter                |
|ETTERLATTEPENSJON |Etterlattepensjon                     |Familiepleier-Særtillegg             |
|ETTERLATTEPENSJON |Etterlattepensjon                     |Familiepleier-Særtllegg              |
|ETTERLATTEPENSJON |Etterlattepensjon                     |Gjenl.-Garantitill. (EØS)            |
|ETTERLATTEPENSJON |Etterlattepensjon                     |Gjenl.-Garantitillegg                |
|ETTERLATTEPENSJON |Etterlattepensjon                     |Gjenlevende yrkesskade               |
|ETTERLATTEPENSJON |Etterlattepensjon                     |Gjenlevende-Fam.tillegg              |
|ETTERLATTEPENSJON |Etterlattepensjon                     |Gjenlevende-Faste utg.               |
|ETTERLATTEPENSJON |Etterlattepensjon                     |Gjenlevende-Grunnpensjon             |
|ETTERLATTEPENSJON |Etterlattepensjon                     |Gjenlevende-Særtillegg               |
|ETTERLATTEPENSJON |Etterlattepensjon                     |Gjenlevende-Tilleggsp.               |
|ETTERLATTEPENSJON |Etterlattepensjon                     |GJENLEVENDE FAMILIEPLEIER            |
|ETTERLATTEPENSJON |Etterlattepensjon                     |GJENLEVENDE YRKESSKADE               |
|ETTERLATTEPENSJON |Etterlattepensjon                     |JENLEVENDE FAMILIEPLEIER             |
|FORELDREPENGER |Foreldrepenger |renter ved etterbetaling             |
|FORELDREPENGER |Foreldrepenger |Adopsjonsstøtte                      |
|FORELDREPENGER |Foreldrepenger |Enslig forsørger                     |
|FORELDREPENGER |Foreldrepenger |Enslig forsørgerl.                   |
|FORELDREPENGER |Foreldrepenger |Fødselsengangsstønad                 |
|FORELDREPENGER |Foreldrepenger |Fødselsengangsstønad adop            |
|FORELDREPENGER |Foreldrepenger |Fødselspenger                        |
|FORELDREPENGER |Foreldrepenger |Fødselspenger fiskere                |
|FORELDREPENGER |Foreldrepenger |Fødselsstønad                        |
|FORELDREPENGER |Foreldrepenger |Fedrekvote |
|FORELDREPENGER |Foreldrepenger |Fedrekvote arbeidsledig              |
|FORELDREPENGER |Foreldrepenger |Fedrekvote arbeidstaker              |
|FORELDREPENGER |Foreldrepenger |Fedrekvote dagmamma                  |
|FORELDREPENGER |Foreldrepenger |Fedrekvote fiskere                   |
|FORELDREPENGER |Foreldrepenger |Fedrekvote frilanser                 |
|FORELDREPENGER |Foreldrepenger |Fedrekvote jordbruker                |
|FORELDREPENGER |Foreldrepenger |Fedrekvote selvst. nær.              |
|FORELDREPENGER |Foreldrepenger |Fedrekvote selvstendig               |
|FORELDREPENGER |Foreldrepenger |Fedrekvote sjømannfradrag            |
|FORELDREPENGER |Foreldrepenger |Fedrekvote sjømenn                   |
|FORELDREPENGER |Foreldrepenger |Feriepenger-svangerskpng             |
|FORELDREPENGER |Foreldrepenger |Foreldrep.  arbeidsledig             |
|FORELDREPENGER |Foreldrepenger |Foreldrep. arbeidsledig              |
|FORELDREPENGER |Foreldrepenger |Foreldrep. arbeidstaker              |
|FORELDREPENGER |Foreldrepenger |Foreldrep. selvstendig               |
|FORELDREPENGER |Foreldrepenger |Foreldrep. sjømannsfradr             |
|FORELDREPENGER |Foreldrepenger |Foreldrep. statsansatt               |
|FORELDREPENGER |Foreldrepenger |Foreldrepenger                       |
|FORELDREPENGER |Foreldrepenger |Foreldrepenger  fribeløp             |
|FORELDREPENGER |Foreldrepenger |Foreldrepenger  selvst.              |
|FORELDREPENGER |Foreldrepenger |Foreldrepenger fiskere               |
|FORELDREPENGER |Foreldrepenger |Foreldrepenger fribeløp              |
|FORELDREPENGER |Foreldrepenger |Foreldrepenger frilanser             |
|FORELDREPENGER |Foreldrepenger |Foreldrepenger mødrekvote            |
|FORELDREPENGER |Foreldrepenger |Foreldrepenger sjømenn               |
|FORELDREPENGER |Foreldrepenger |Foreldrepenger statsans.             |
|FORELDREPENGER |Foreldrepenger |Foreldrepenger FFU                   |
|FORELDREPENGER |Foreldrepenger |Foreldrepenger Svalbard              |
|FORELDREPENGER |Foreldrepenger |Jordbruker |
|FORELDREPENGER |Foreldrepenger |Mødrekvote arbeidstaker              |
|FORELDREPENGER |Foreldrepenger |Mødrekvote dagmamma                  |
|FORELDREPENGER |Foreldrepenger |Mødrekvote fiskere                   |
|FORELDREPENGER |Foreldrepenger |Mødrekvote frilanser                 |
|FORELDREPENGER |Foreldrepenger |Mødrekvote jordbruker                |
|FORELDREPENGER |Foreldrepenger |Mødrekvote selvst. næring            |
|FORELDREPENGER |Foreldrepenger |Mødrekvote selvstendig               |
|FORELDREPENGER |Foreldrepenger |Mødrekvote sjøm.fradrag              |
|FORELDREPENGER |Foreldrepenger |Mødrekvote sjømannsfradr.            |
|FORELDREPENGER |Foreldrepenger |Mødrekvote v/arb.ledigh              |
|FORELDREPENGER |Foreldrepenger |Mødrekvote v/arb.ledighet            |
|FORELDREPENGER |Foreldrepenger |Omsorgspenger adopsjon               |
|FORELDREPENGER |Foreldrepenger |RENTER VED ETTERBETALING             |
|FORELDREPENGER |Foreldrepenger |Svangersk.p. arbeidsledig            |
|FORELDREPENGER |Foreldrepenger |Svangersk.p. arbeidstaker            |
|FORELDREPENGER |Foreldrepenger |Svangersk.p. selvstendig             |
|FORELDREPENGER |Foreldrepenger |Svangerskapspenger                   |
|FORSKUDDSTREKK |Forskuddstrekk |Forskuddstrekk                       |
|FORSKUDDSTREKK |Forskuddstrekk |Forskuddstrekk barnepensjon          |
|FORSKUDDSTREKK |Forskuddstrekk |Forskuddstrekk frivillig             |
|FORSKUDDSTREKK |Forskuddstrekk |Forskuddstrekk frivillig barnepensjon|
|FORSKUDDSTREKK |Forskuddstrekk |Forskuddstrekk frivillig Svalbard    |
|FORSKUDDSTREKK |Forskuddstrekk |Forskuddstrekk ordinært              |
|FORSKUDDSTREKK |Forskuddstrekk |Forskuddstrekk Kildeskatt på pensjon |
|FORSKUDDSTREKK |Forskuddstrekk |Forskuddstrekk Svalbard              |
|KRIGSPENSJON|Krigspensjon      |Barnepensjon                         |
|KRIGSPENSJON|Krigspensjon      |Barnepensjon militær                 |
|KRIGSPENSJON|Krigspensjon      |Barnetillegg                         |
|KRIGSPENSJON|Krigspensjon      |Ektefelletillegg                     |
|KRIGSPENSJON|Krigspensjon      |Etterlattep. militær                 |
|KRIGSPENSJON|Krigspensjon      |Etterlattepensjon                    |
|KRIGSPENSJON|Krigspensjon      |Hjelpeløshetsbidrag                  |
|KRIGSPENSJON|Krigspensjon      |Invalidepensjon                      |
|KRIGSPENSJON|Krigspensjon      |Invalidepensjon, militær             |
|KRIGSPENSJON|Krigspensjon      |Invalidepensjon, sivil               |
|KRIGSPENSJON|Krigspensjon      |Krigspensj. sivil Mèndel             |
|KRIGSPENSJON|Krigspensjon      |Krigspensjon                         |
|KRIGSPENSJON|Krigspensjon      |Krigspensjon militær                 |
|KRIGSPENSJON|Krigspensjon      |Krigspensjon. sivil mèndel           |
|KRIGSPENSJON|Krigspensjon      |Krigspensjon, Mèn del                |
|KRIGSPENSJON|Krigspensjon      |Militær skadeskur                    |
|KRIGSPENSJON|Krigspensjon      |Sivil skadekur                       |
|KRIGSPENSJON|Krigspensjon      |Till. hjelp i huset                  |
|KRIGSPENSJON|Krigspensjon      |Till.hjelp i huset                   |
|OMSORGSPENGER  |Omsorgspenger  |renter     |
|OMSORGSPENGER  |Omsorgspenger  |Omsorgspenger                        |
|OPPLAERINGSPENGER |Opplæringspenger                      |renter     |
|OPPLAERINGSPENGER |Opplæringspenger                      |Opplæringspenger                     |
|OPPLAERINGSPENGER |Opplæringspenger                      |Opplæringspenger avdød               |
|PLEIEPENGER |Pleiepenger       |Pleiepenger|
|PLEIEPENGER |Pleiepenger       |Pleiepenger avdød                    |
|PLEIEPENGER |Pleiepenger       |Pleiepenger pårørende                |
|PLEIEPENGER |Pleiepenger       |Pleiepenger sykt barn                |
|SPK   |Statens pensjonkasse    |Alderspensjon før 2020               |
|SPK   |Statens pensjonkasse    |Alderspensjon fra 2020               |
|SPK   |Statens pensjonkasse    |Betinget tjenestepensjon             |
|SPK   |Statens pensjonkasse    |Livsvarig AFP                        |
|SPK   |Statens pensjonkasse    |Overgangstillegg                     |
|SPK   |Statens pensjonkasse    |Pensjon og livrenter i arbeidsforhold|
|SPK   |Statens pensjonkasse    |Statens pensjonskasse                |
|SPK   |Statens pensjonkasse    |SPK -Alderpensjon                    |
|SPK   |Statens pensjonkasse    |SPK Engangsutbetaling til etterlatte |
|SPK   |Statens pensjonkasse    |SPK-AFP    |
|SPK   |Statens pensjonkasse    |SPK-Barnepensjon                     |
|SPK   |Statens pensjonkasse    |SPK-Gjenlevende ektefelle            |
|SPK   |Statens pensjonkasse    |SPK-Renter |
|SPK   |Statens pensjonkasse    |SPK-Uførepensjon                     |
|SVANGERSKAPSPENGER|Svangerskapspenger                    |Feriepenger-foreldrepng              |
|SVANGERSKAPSPENGER|Svangerskapspenger                    |Svangerskapspenger                   |
|SYKEPENGER  |Sykepenger        |Akt./arb.trening                     |
|SYKEPENGER  |Sykepenger        |Avbrudd YA ordinær                   |
|SYKEPENGER  |Sykepenger        |Avbrudd YA yrkesskade                |
|SYKEPENGER  |Sykepenger        |Avbrudd YA EØS                       |
|SYKEPENGER  |Sykepenger        |Barnetillegg ordinær                 |
|SYKEPENGER  |Sykepenger        |Barnetillegg syk student             |
|SYKEPENGER  |Sykepenger        |Barnetillegg ventetid UY             |
|SYKEPENGER  |Sykepenger        |Barnetillegg yrkesskade              |
|SYKEPENGER  |Sykepenger        |Barnetillegg EØS                     |
|SYKEPENGER  |Sykepenger        |Feriep fødsel/adopsjon               |
|SYKEPENGER  |Sykepenger        |Feriep.sykepenger m.v.               |
|SYKEPENGER  |Sykepenger        |Feriepenger fødselspenger            |
|SYKEPENGER  |Sykepenger        |Feriepenger statsansatt              |
|SYKEPENGER  |Sykepenger        |Feriepenger sykepenger               |
|SYKEPENGER  |Sykepenger        |Ferietillegg sykepenger              |
|SYKEPENGER  |Sykepenger        |Forskudd feriepenger                 |
|SYKEPENGER  |Sykepenger        |Forskudd ferietillegg                |
|SYKEPENGER  |Sykepenger        |Frilanser  |
|SYKEPENGER  |Sykepenger        |Jordbruker |
|SYKEPENGER  |Sykepenger        |Omsorgspenger                        |
|SYKEPENGER  |Sykepenger        |Omsorgspenger Svalbard               |
|SYKEPENGER  |Sykepenger        |Opplæringsp arbeidsledig             |
|SYKEPENGER  |Sykepenger        |Opplæringsp arbeidstaker             |
|SYKEPENGER  |Sykepenger        |Opplæringsp selvstendig              |
|SYKEPENGER  |Sykepenger        |Opplæringsp Svalbard                 |
|SYKEPENGER  |Sykepenger        |Pleiepenger|
|SYKEPENGER  |Sykepenger        |Pleiepenger arbeidsledig             |
|SYKEPENGER  |Sykepenger        |Pleiepenger arbeidstaker             |
|SYKEPENGER  |Sykepenger        |Pleiepenger dagmamma                 |
|SYKEPENGER  |Sykepenger        |Pleiepenger selvst nær               |
|SYKEPENGER  |Sykepenger        |Pleiepenger selvstendig              |
|SYKEPENGER  |Sykepenger        |Pleip.nær.pårørende                  |
|SYKEPENGER  |Sykepenger        |Rehabiliteringspenger                |
|SYKEPENGER  |Sykepenger        |Renter etterbetaling                 |
|SYKEPENGER  |Sykepenger        |Selvst. dagmamma fra 17d             |
|SYKEPENGER  |Sykepenger        |Selvst. dagmamma 1-16d               |
|SYKEPENGER  |Sykepenger        |Selvstendig dagmamma                 |
|SYKEPENGER  |Sykepenger        |Spsforeldre m/syke barn              |
|SYKEPENGER  |Sykepenger        |Syk student ordinær                  |
|SYKEPENGER  |Sykepenger        |Syk student yrkesskade               |
|SYKEPENGER  |Sykepenger        |Syk student EØS                      |
|SYKEPENGER  |Sykepenger        |Sykep akiv/arbeidstrening            |
|SYKEPENGER  |Sykepenger        |Sykep aktiv/arbeidstr                |
|SYKEPENGER  |Sykepenger        |Sykep forsk i arbg per               |
|SYKEPENGER  |Sykepenger        |Sykep friv trygdet FFU               |
|SYKEPENGER  |Sykepenger        |Sykep v/arbeidsformidling            |
|SYKEPENGER  |Sykepenger        |Sykep yrkesrettet attf               |
|SYKEPENGER  |Sykepenger        |Sykep. selvst. næring                |
|SYKEPENGER  |Sykepenger        |Sykepenger |
|SYKEPENGER  |Sykepenger        |Sykepenger arbeidsledig              |
|SYKEPENGER  |Sykepenger        |Sykepenger arbeidstaker              |
|SYKEPENGER  |Sykepenger        |Sykepenger arbeidstakere             |
|SYKEPENGER  |Sykepenger        |Sykepenger dagmamma                  |
|SYKEPENGER  |Sykepenger        |Sykepenger inaktiv                   |
|SYKEPENGER  |Sykepenger        |Sykepenger inaktiv FFU               |
|SYKEPENGER  |Sykepenger        |Sykepenger militær                   |
|SYKEPENGER  |Sykepenger        |Sykepenger offshoreansatt            |
|SYKEPENGER  |Sykepenger        |Sykepenger selvstendig               |
|SYKEPENGER  |Sykepenger        |Sykepenger sjømann FFU               |
|SYKEPENGER  |Sykepenger        |Sykepenger til fisker                |
|SYKEPENGER  |Sykepenger        |Sykepenger til jord- og skogbrukere  |
|SYKEPENGER  |Sykepenger        |Sykepenger ved yrkesskade            |
|SYKEPENGER  |Sykepenger        |Sykepenger FFU                       |
|SYKEPENGER  |Sykepenger        |Sykepenger Svalbard                  |
|SYKEPENGER  |Sykepenger        |Sykepenger-renter                    |
|SYKEPENGER  |Sykepenger        |SPSFORELDRE M/SYKE BARN              |
|SYKEPENGER  |Sykepenger        |SYKEPENGER ARBEIDSTAKERE             |
|SYKEPENGER  |Sykepenger        |Tilbakefall ordinær                  |
|SYKEPENGER  |Sykepenger        |Tilbakefall yrkesskade               |
|SYKEPENGER  |Sykepenger        |Tilbakefall EØS                      |
|SYKEPENGER  |Sykepenger        |Under arb.trening ordinær            |
|SYKEPENGER  |Sykepenger        |Under arb.trening yrkessk            |
|SYKEPENGER  |Sykepenger        |Under arb.trening EØS                |
|SYKEPENGER  |Sykepenger        |Uten sykep.rett ordinær              |
|SYKEPENGER  |Sykepenger        |Uten sykep.rett yrkessk.             |
|SYKEPENGER  |Sykepenger        |Uten sykep.rett EØS                  |
|SYKEPENGER  |Sykepenger        |Ventetid tiltak ordinær              |
|SYKEPENGER  |Sykepenger        |Ventetid tiltak yrkessk.             |
|SYKEPENGER  |Sykepenger        |Ventetid tiltak EØS                  |
|SYKEPENGER  |Sykepenger        |Ventetid UY ordinær                  |
|TJENESTEPENSJON|Tjenestepensjon|Bykassetilskudd                      |
|TJENESTEPENSJON|Tjenestepensjon|Kommunal barnetillegg                |
|TJENESTEPENSJON|Tjenestepensjon|Kommunal tilleggspensjon             |
|TJENESTEPENSJON|Tjenestepensjon|Livsvarig avtalefestet pensjon (AFP) i offentlig sektor     |
|TJENESTEPENSJON|Tjenestepensjon|Pensjon og livrenter i arbeidsforhold|
|TJENESTEPENSJON|Tjenestepensjon|Pensjon, jernbanen                   |
|TJENESTEPENSJON|Tjenestepensjon|PTS -Alderspensjon                   |
|TJENESTEPENSJON|Tjenestepensjon|PTS -Barnepensjon                    |
|TJENESTEPENSJON|Tjenestepensjon|PTS-Gjenlevende ektefelle            |
|UFOREPENSJON|Uførepensjon      |Barnepensjon                         |
|UFOREPENSJON|Uførepensjon      |Barnetillegg                         |
|UFOREPENSJON|Uførepensjon      |Eøs        |
|UFOREPENSJON|Uførepensjon      |Etterbetaling                        |
|UFOREPENSJON|Uførepensjon      |Forel.UP-Ektefelletillegg            |
|UFOREPENSJON|Uførepensjon      |Foreløpig UP-Barnetillegg            |
|UFOREPENSJON|Uførepensjon      |Foreløpig UP-Familietill.            |
|UFOREPENSJON|Uførepensjon      |Foreløpig UP-Faste utg.              |
|UFOREPENSJON|Uførepensjon      |Foreløpig UP-Garantitill.            |
|UFOREPENSJON|Uførepensjon      |Foreløpig UP-Grunnpensjon            |
|UFOREPENSJON|Uførepensjon      |Foreløpig UP-Tilleggsp.              |
|UFOREPENSJON|Uførepensjon      |Gml. yrkesskade gjenlev.             |
|UFOREPENSJON|Uførepensjon      |GML. YRKESSKADE GJENLEV.             |
|UFOREPENSJON|Uførepensjon      |Mèn del    |
|UFOREPENSJON|Uførepensjon      |Tidsbegr. uførestønad ord            |
|UFOREPENSJON|Uførepensjon      |Tidsbegrenset uføre                  |
|UFOREPENSJON|Uførepensjon      |TIDSBEGRENSET UFØRE                  |
|UFOREPENSJON|Uførepensjon      |Uføre- ektefelletillegg              |
|UFOREPENSJON|Uførepensjon      |Uføre-Barnetillegg                   |
|UFOREPENSJON|Uførepensjon      |Uføre-Ektefelletillegg               |
|UFOREPENSJON|Uførepensjon      |Uføre-Fam.tillegg(inst.)             |
|UFOREPENSJON|Uførepensjon      |Uføre-Faste utg.(inst.)              |
|UFOREPENSJON|Uførepensjon      |Uføre-Garantitillegg                 |
|UFOREPENSJON|Uførepensjon      |Uføre-Garantitillegg(EØS)            |
|UFOREPENSJON|Uførepensjon      |Uføre-Grunnpensjon                   |
|UFOREPENSJON|Uførepensjon      |Uføre-Minstenivåtillegg              |
|UFOREPENSJON|Uførepensjon      |Uføre-Særtillegg                     |
|UFOREPENSJON|Uførepensjon      |Uføre-Tilleggspensj.                 |
|UFOREPENSJON|Uførepensjon      |Uførepensjon                         |
|UFOREPENSJON|Uførepensjon      |Uførepensjon yrkesskade              |
|UFOREPENSJON|Uførepensjon      |Uførepensjon, barnetill.             |
|UFOREPENSJON|Uførepensjon      |Uførepensjon, grunnpensj.            |
|UFOREPENSJON|Uførepensjon      |Uførepensjon, særtillegg             |
|UFOREPENSJON|Uførepensjon      |Uførepensjon, tilleggsp.             |
|UFOREPENSJON|Uførepensjon      |Uførepensjon, §8-5 till.             |
|UFOREPENSJON|Uførepensjon      |Utløsningsbeløp (g 11-7)             |
|UFOREPENSJON|Uførepensjon      |UFØREPENSJON                         |
|UFOREPENSJON|Uførepensjon      |Yrkesskadetrygd                      |
|UFOREPENSJON|Uførepensjon      |Yrkesskadetrygd gjenl.               |
|UFOREPENSJON|Uførepensjon      |Yrkesskadetrygd Barnetill            |
|UFORETRYGD  |Uføretrygd        |Uføretrygd |
|UFORETRYGD  |Uføretrygd        |Uføretrygd, barnetillegg             |
|UFORETRYGD  |Uføretrygd        |Uføretrygd, garantitill.             |
|UFORETRYGD  |Uføretrygd        |Uføretrygd, renter                   |
|UFORETRYGD  |Uføretrygd        |Uføretrygd,ektefelletill.            |
|UFORETRYGD  |Uføretrygd        |Uføretrygd,faste utg.inst            |
|UFORETRYGD  |Uføretrygd        |Uføretrygd,gjenlev.till.             |
|UFORETRYGD  |Uføretrygd        |Uføretrygd,motregning AAP            |
|UFORETRYGD  |Uføretrygd        |Uføretrygd,motregning SP             |
|VEDERLAGSTREKK |Vederlagstrekk |Vederlagstrekk                       |

