/* kixi.paloma.enrich - Base SQL queries for BX Database (Postgres) */

-- :name create-business-record! :! :n
INSERT INTO business
(uprn, nndr_prop_ref, start_date, end_date)
VALUES (:uprn, :nndr_prop_ref, :start_date, :end_date)

-- :name create-address-record! :! :n
INSERT INTO address
(uprn, data_source, premises_ref, address_fields, postcode)
VALUES (:uprn, :data_source, :premises_ref, :address_fields, :postcode)

-- :name create-business-name-record! :! :n
INSERT INTO business_name
(uprn, data_source, premises_ref, civica_preferred_name, business_name, update_date, start_date, end_date)
VALUES (:uprn, :data_source, :premises_ref, :civica_preferred_name, :business_name, :update_date, :start_date, :end_date)
