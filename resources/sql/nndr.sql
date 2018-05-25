/* kixi.paloma.enrich - Base SQL Queries */

-- :name get-nndr-records :? :*
-- :doc Fetches all the nndr records (and joined tables).
SELECT
"nndr" as data_source,
a.*,
d.propref as nndr_prop_ref,
vo_propdescrip as property_description,
	1 as counter_nndr_record
  FROM [NRLIVEDB].[dbo].[nraccount] a
  right join  [NRLIVEDB].[dbo].nrchargerec b
  on a.account_id=b.account_id
  left join  [NRLIVEDB].[dbo].[nrproplink] c
  on a.account_id=c.account_id and a.[check_digit]=c.[check_digit]
  left join  [NRLIVEDB].[dbo].nrproperty d
  on c.propref=d.propref
    left join [NRLIVEDB].[dbo].nrrvhist e
  on  c.propref=e.propref
  where b.date_from != b.date_to
  and b.date_to>getdate()
  and   e.date_to >getdate()
