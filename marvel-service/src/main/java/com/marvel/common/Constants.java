package com.marvel.common;

public interface Constants {
	public static final String SQL_GETBYNAMEANDSERIEANDSTORY = ""
		+ " select distinct id, character_code, description, image, name from "
		+ "	(select "
		+ "		c.*, "
		+ "		ifnull(s.name, 'null') seriename, "
		+ "        ifnull(sto.name, 'null') storyname "
		+ "        from characters c"
		+ "		left join character_series cs on c.id = cs.character_id"
		+ "		left join series s on s.id = cs.serie_id"
		+ "		left join character_stories cst on c.id = cst.character_id "
		+ "		left join stories sto on sto.id = cst.story_id"
		+ "        group by seriename, storyname) x"
		+ " where upper(name) like upper(ifnull(?1, name))"
		+ " and  upper(seriename) like upper(ifnull(?2, seriename))"
		+ " and upper(storyname) like upper(ifnull(?3, storyname))";
	public static final String MSG_NOTFOUND = "Record not found.";
	public static final String MSG_CONFLIC_ERROR = "something went wrong, if you don't know contact your administrator.";
}
