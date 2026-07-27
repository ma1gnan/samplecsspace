sale(
    sale_id INTEGER PRIMARY KEY,
    property_id INTEGER,
    buyer_id INTEGER,
    seller_id INTEGER,
    selling_agent INTEGER,
    buyers_agent INTEGER,
    sale_price REAL,
    sale_date TEXT
)