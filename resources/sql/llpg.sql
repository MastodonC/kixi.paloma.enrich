/* kixi.paloma.enrich - Base SQL Queries */
-- :name get-llpg-records :? :*
-- :doc gets all the LLPG records to be enriched.
SELECT distinct
      [lpit].[uprn] as UPRN
           ,[lpit].[uprn]  as llpg_uprn
      ,[blpu].[organisation]
      ,[blpu].[start_date]
      ,[blpu].[entry_date]
      ,[blpu].[last_update_date]
      ,[blpu].[end_date]
      ,[lpit].[address]
	  ,postcode as postcode_master
    --  ,[lpit].[bs7666_address]
	  ,[CROSS_REFERENCE] as nndr_prop_ref
	  ,1 as counter_llpg_record
	  ,blp.USAGE as llpg_usage

  FROM [BS7666_LBHACKNEY].[dbo].[All_LPI_Types] AS lpit
        JOIN [BS7666_LBHACKNEY].[dbo].[All_BLPU_Types] AS blpu
        ON ([blpu].[uprn] = [lpit].[uprn])
		left join (select * from [BS7666_LBHACKNEY].[dbo].[BLPU_APP_CROSS_REF] where [SOURCE]='2') xref
		on lpit.[uprn]=xref.[uprn]

		left join [BS7666_LBHACKNEY].[dbo].[LLPG_Postal_Address] pc
	on lpit.[uprn]=pc.[uprn]
	left join [BS7666_LBHACKNEY].[dbo].[BLPU] blp
	on lpit.[uprn]=blp.[uprn]
				where blpu.blpu_class like 'C%' and blpu.[neverexport]=0
				 and
		  [lpit].[logical_status] in (1,6)
