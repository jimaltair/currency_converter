INSERT INTO currency
    (num_code, char_code, nominal, name)
VALUES
    ('643', 'RUB', 1, 'Российский рубль')
ON CONFLICT (num_code) DO NOTHING;

INSERT INTO currency_rate
    (id, rate, date, currency_num_code)
VALUES
    (1, 1.0, '2022-01-28', 643)
ON CONFLICT (id) DO NOTHING;