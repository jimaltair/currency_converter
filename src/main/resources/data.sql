INSERT INTO currency
    (num_code, char_code, nominal, name)
VALUES
    ('643', 'RUB', 1, 'Российский рубль')
ON CONFLICT (num_code) DO NOTHING;

INSERT INTO currency_rate
    (id, rate, date, currency_num_code)
VALUES
    (1, 1.0, '1970-01-01', 643)
ON CONFLICT (id) DO NOTHING;

SELECT setval('currency_rate_id_seq', (SELECT MAX(id) FROM currency_rate));