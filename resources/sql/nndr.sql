/* kixi.paloma.enrich - Base SQL Queries */

-- :name get-nndr-records :? :*
-- :doc Fetches all the nndr records (and joined tables).
SELECT
case when account_holder2 like ('T/AS %')
	or account_holder2 like ('T/A %')
	or account_holder2 like ('T/A. %') then  replace(replace(replace(account_holder2,'T/AS ',''),'T/A ',''),'T/A. ','T/A. ')

when account_holder1 like ('MR %')
	 or account_holder1 like ('MS %')
	 or account_holder1 like ('MRS %')
	 or account_holder1 like ('MISS %')
	 or account_holder1 like ('ATTN %')
	 or account_holder1 like ('FAO %')
	 or account_holder1 like ('FAO: %')
	 or account_holder1 like ('C/O: %')
	 or account_holder1 like ('DR %')
	 or account_holder1 like ('REV %')
	 or account_holder1 like ('CLLR %')
	 or account_holder1 like ('DR %')
	 or account_holder1 like ('ELDER %')
	 or account_holder1 like ('error')
	 or account_holder1 like ('FR %')
	 or account_holder1 like ('GEN %')
	 or account_holder1 like ('LADY %')
	 or account_holder1 like ('LORD %')
	 or account_holder1 like ('MADAM %')
	 or account_holder1 like ('MAJ %')
	 or account_holder1 like ('MASTER %')
	 or account_holder1 like ('MX %')
	 or account_holder1 like ('PC %')
	 or account_holder1 like ('PR %')
	 or account_holder1 like ('PROF %')
	 or account_holder1 like ('RABBI %')
	 or account_holder1 like ('SGT %')
	 or account_holder1 like ('SIR %')
	 or account_holder1 like ('SR %')
			 then 'Name Redacted'else replace(account_holder1,'T/AS','') end as Business_name,
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
  and   e.date_to >getdate();
