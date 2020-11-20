<세탁기 사용>
update Washer set washer_state = 1, last_usr_num={사용자학번}, end_time={끝나는시각} where washer_num={선택된 세탁기번호} and washer_state=0
insert into Log (usr_num, start_time, washer_num) values ({학번}, {현재시각}, {선택된 세탁기 번호})