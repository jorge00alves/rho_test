# rho_test

Start api guide:

1- Open project with your IDE of choice (example: NetBeans);

2- Run project;

3- Open browser and type in the following URL: "localhost:8080", followed by one of theese options:

3.1- "/getA2B?a=<base currency>&b=<currency>" (example: "/getA2B?a=eur&b=jpy");

3.2- "/getAllFromA?a=<base currency>" (example: "/getAllFromA?a=eur");

3.3- "/convertA2B?a=<base currency>&b=<currency>&c=<amount of base currency>" (example: /convertA2B?a=eur&b=gbp&c=10);

3.4- "convertA2SuppliedList?a=<base currency>&b=<list of currencies separeted by ','>&c=<amount of base currency>" (example: convertA2SuppliedList?a=eur&b=usd,gbp,jpy&c=15);


Notes:

- Only got data from fixer.io free subscription and, because of this, can only get data when base currency is "eur". When fetching data with other base currency we get an error: "base_currency_access_restricted";

- Database is storing only one registry of each kind (example: only storing 1 exchange rate of base currency "EUR" and currency "USD");

- When executing a request, if the required registries on the database, are older or equal to 30 minuts, we will fetch data again from fixer.io;

- Failed on creating documentation with swagger. Though methods are documented and some code is commented.