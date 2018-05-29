/* kixi.paloma.enrich - Base SQL Queries */

-- :name get-civica-records :? :*
-- :doc Returns records from Civica mirror.
select
      [Ptr] as premises_id
      ,[Ref] as premises_ref
      ,[Name1] as civica_name
	  ,coalesce(latest_name,[Name1]) as preferred_name
      ,[Namecode1]
      ,[Name2]
      ,[Namecode2]
      ,[Lpikey]
      ,[Housename]
      ,[Houseno]
      ,[Add1]
      ,[Add2]
      ,[Add3]
      ,[Add4]
      ,[Postcode]
      ,prem.[Uprn] as UPRN
   FROM [FlareLive].[dbo].[premises] prem
   left join (Select uprn,
PremisesName as latest_name
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

[ActivityType]='M' and PremisesName<>'' and ActivityCategoryCode!='LTN'
--and uprn='10008305333'

  ) f
  where rowno=1 ) latest
  on prem.uprn=latest.uprn

    where prem.uprn<>'' and ([Name1]<>'' or  latest.latest_name<>'') and [Seq]=1
	order by UPRN
