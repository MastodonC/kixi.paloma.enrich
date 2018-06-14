/* kixi.paloma.enrich - Base SQL Queries */

-- :name get-civica-records :? :*
-- :doc Returns records from Civica mirror.
select
   [Ptr] as premises_id

      ,[Ref] as premises_ref
      ,[Name1] as civica_name
	  ,coalesce(latest_name,[Name1]) as preferred_name
    --  ,[Namecode1]
      ,[Name2]
--,[Namecode2]
      ,[Lpikey]
      ,[Housename]
      ,[Houseno]
      ,[Add1]
      ,[Add2]
      ,[Add3]
      ,[Add4]
      ,[Postcode]
      ,prem.[Uprn] as UPRN
	  ,Ladate as last_seen_date
	  ,null_ref_count
	  ,prem_count
	  ,last_activity

   FROM [FlareLive].[dbo].[premises] prem
   left join (Select uprn,
PremisesName as latest_name,
[OpenedDate] as last_activity
 from (
select
uprn,
PremisesName,
PremisesId,
[OpenedDate],
ROW_NUMBER() over (PARTITION  by uprn order by [OpenedDate] desc) as rowno
  FROM [FlareLive].[dbo].[vw_Activity] a
  left join   [FlareLive].[dbo].[premises] b
  on a.PremisesId=b.ptr
   where

[ActivityType]!='R' and PremisesName<>'' and ActivityCategoryCode!='LTN'


  ) f
  where rowno=1

  ) latest
  on prem.uprn=latest.uprn

  left join(
  Select UPRN,
  count(distinct Ref) as prem_count
  FROM [FlareLive].[dbo].[premises]
 where [Seq]=1 and [Name1]<>''
  group by UPRN) prop_count
  on prem.uprn=prop_count.uprn
    left join(
  Select UPRN,
  count(distinct Ref) as null_ref_count
  FROM [FlareLive].[dbo].[premises]
 where Ladate is null and [Seq]=1 and [Name1]<>''
  group by UPRN) null_ref
  on prem.uprn=null_ref.uprn

    where prem.uprn<>''
	and ([Name1]<>'' or  latest.latest_name<>'') and [Seq]=1
	and left(Ref,3)<>'***'
	and ((prem_count=1 and null_ref_count=1) or Ladate is not null)

		order by UPRN
