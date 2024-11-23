CREATE TABLE PRODUCT
(
  /*
   * Data type serial automatically creates a sequence <table_name>_<column>_seq (role_id_seq)
   */
    id SERIAL       PRIMARY KEY,
    artist          VARCHAR(255)    NOT NULL,
    album_title     VARCHAR(255)    NOT NULL,
    release_year    INTEGER         NOT NULL,
    price           DECIMAL(10, 2)  NOT NULL,
    promo_code      BYTEA           NULL,   -- Encrypted promo code
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);