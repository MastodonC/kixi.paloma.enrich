/* kixi.paloma.enrich - Base SQL queries for BX Database (Postgres) */

-- :name create-business-record! :! :n
INSERT INTO :i:btname
(uprn, nndr_prop_ref, start_date, end_date)
VALUES (:uprn, :nndr_prop_ref, :start_date, :end_date)

-- :name create-address-record! :! :n
INSERT INTO :i:atname
(uprn, data_source, premises_ref, address_fields, postcode)
VALUES (:uprn, :data_source, :premises_ref, :address_fields, :postcode)

-- :name create-business-name-record! :! :n
INSERT INTO :i:bntname
(uprn, data_source, premises_ref, civica_preferred_name, business_name, update_date, start_date, end_date)
VALUES (:uprn, :data_source, :premises_ref, :civica_preferred_name, :business_name, :update_date, :start_date, :end_date)

-- :name get-business-records :? :*
SELECT * FROM business LIMIT 100;

-- :name create-business-table! :! :n
CREATE TABLE :i:btname (
       uprn varchar(40) NOT NULL,
       nndr_prop_ref varchar(40),
       start_date date,
       end_date date);

-- :name create-business-name-table! :! :n
CREATE TABLE :i:bntname (
       uprn varchar(40) NOT NULL,
       data_source varchar(100) NOT NULL,
       premises_ref varchar(40),
       civica_preferred_name boolean,
       business_name varchar(255),
       update_date date,
       start_date date,
       end_date date);

-- :name create-address-table! :! :n
CREATE TABLE :i:atname (
       uprn varchar(40) NOT NULL,
       data_source varchar(100) NOT NULL,
       premises_ref varchar(40),
       address_fields varchar(255),
       postcode varchar(20));

-- :name create-business-view! :! :n
CREATE OR REPLACE VIEW business AS SELECT * FROM :i:btname

-- :name create-address-view! :! :n
CREATE OR REPLACE VIEW address AS SELECT * FROM :i:atname

-- :name create-business-name-view! :! :n
CREATE OR REPLACE VIEW business_name AS SELECT * FROM :i:bntname
