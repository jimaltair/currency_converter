<!DOCTYPE html>
<html lang="en">
<#import "spring.ftl" as spring/>
<head>
    <meta charset="UTF-8">
    <title>Курсы валют</title>
    <script>
        function CBR_XML_Daily_Ru(rates) {
            function trend(current, previous) {
                if (current > previous) return ' ▲';
                if (current < previous) return ' ▼';
                return '';
            }

            var USDrate = rates.Valute.USD.Value.toFixed(4).replace('.', ',');
            var USD = document.getElementById('USD');
            USD.innerHTML = USD.innerHTML.replace('00,0000', USDrate);
            USD.innerHTML += trend(rates.Valute.USD.Value, rates.Valute.USD.Previous);

            var EURrate = rates.Valute.EUR.Value.toFixed(4).replace('.', ',');
            var EUR = document.getElementById('EUR');
            EUR.innerHTML = EUR.innerHTML.replace('00,0000', EURrate);
            EUR.innerHTML += trend(rates.Valute.EUR.Value, rates.Valute.EUR.Previous);
        }
    </script>
    <link rel="dns-prefetch" href="https://www.cbr-xml-daily.ru/" />
    <script src="//www.cbr-xml-daily.ru/daily_jsonp.js" async></script>
</head>
<body>
<div id="USD">Доллар США $ — 00,0000 руб.</div>
<div id="EUR">Евро € — 00,0000 руб.</div>
</body>
</html>