create table if not exists area_roles
(
    area_role_idx int not null comment '구역역할인덱스'
        primary key,
    area_role_name varchar(45) null comment '구역역할명'
)
    comment '구역역할' charset=utf8;

create table if not exists areas
(
    area_idx int auto_increment comment '구역인덱스'
        primary key,
    create_at timestamp not null comment '최초생성일시',
    create_user_idx int not null comment '최초생성사용자인덱스',
    update_at timestamp null comment '최종변경일시',
    update_user_idx int null comment '최종변경사용자인덱스',
    del_yn char not null comment '삭제여부',
    area_name varchar(45) not null comment '구역명',
    area_user_name varchar(45) null comment '담당자명',
    area_vendor_name varchar(100) null comment '업체명',
    area_vendor_phone varchar(45) null comment '연락처',
    area_vendor_address text null comment '주소',
    area_vendor_detail_address text null comment '상세주소',
    area_latitude decimal(16,14) null,
    area_longitude decimal(17,14) null,
    etc text null comment '비고',
    area_title text null,
    tab_title text null,
    backgroud_image_url text null,
    error_color_code varchar(9) null
)
    comment '구역' charset=utf8;

create table if not exists conditions
(
    condition_idx int auto_increment comment '조건인덱스'
        primary key,
    create_at timestamp not null comment '최초생성일시',
    create_user_idx int not null comment '최초생성사용자인덱스',
    update_at timestamp null comment '최종변경일시',
    update_user_idx int null comment '최종변경사용자인덱스',
    del_yn char not null comment '삭제여부',
    condition_name varchar(100) not null comment '조건명',
    condition_data json not null comment '조건정보'
)
    comment '조건' charset=utf8;

create table if not exists dashboard_layouts
(
    area_idx int not null comment '구역인덱스',
    dashboard_order int not null comment '대시보드정렬순서',
    device_serial varchar(40) not null comment '단말기시리억',
    primary key (area_idx, dashboard_order),
    constraint FK_dashboard_layouts1
        foreign key (area_idx) references areas (area_idx)
            on update cascade on delete cascade
)
    comment '대시보드레이아웃' charset=utf8;

create table if not exists device_categories
(
    device_category_idx int auto_increment comment '단말기종류인덱스'
        primary key,
    device_category_name varchar(45) not null comment '단말기종류명'
)
    comment '단말기종류' charset=utf8;

create table if not exists device_comm_types
(
    device_comm_type_idx int auto_increment comment '단말기통신타입인덱스'
        primary key,
    device_comm_type_name varchar(45) not null comment '단말기통신타입명'
)
    comment '단말기통신타입' charset=utf8;

create table if not exists device_log_files
(
    device_log_file_idx int auto_increment
        primary key,
    create_at timestamp(6) not null,
    create_user_idx int not null,
    logical_file_name varchar(200) null,
    physical_file_name text null
)
    comment '장치로그파일' charset=utf8;

create table if not exists device_types
(
    device_type_idx int auto_increment comment '단말기타입인덱스'
        primary key,
    device_type_name varchar(45) not null comment '단말기타입명'
)
    comment '단말기타입' charset=utf8;

create table if not exists device_vendors
(
    device_vendor_idx int auto_increment comment '단말기제조사인덱스'
        primary key,
    device_vendor_name varchar(100) not null comment '단말기제조사명',
    device_vendor_phone varchar(50) null comment '단말기제조사전화번호',
    device_vendor_address text null comment '단말기제조사주소'
)
    comment '단말기제조사' charset=utf8;

create table if not exists device_infos
(
    device_info_idx int auto_increment comment '단말기정보인덱스'
        primary key,
    device_name varchar(50) not null comment '단말기명',
    device_vendor_idx int not null comment '단말기제조사인덱스',
    device_type_idx int not null comment '단말기타입인덱스',
    device_category_idx int not null comment '단말기종류인덱스',
    device_comm_type_idx int not null comment '단말기통신타입인덱스',
    constraint FK_device_infos1
        foreign key (device_type_idx) references device_types (device_type_idx),
    constraint FK_device_infos2
        foreign key (device_category_idx) references device_categories (device_category_idx),
    constraint FK_device_infos3
        foreign key (device_vendor_idx) references device_vendors (device_vendor_idx),
    constraint FK_device_infos4
        foreign key (device_comm_type_idx) references device_comm_types (device_comm_type_idx)
)
    comment '단말기정보' charset=utf8;

create table if not exists devices
(
    device_serial varchar(40) not null comment '단말기시리얼'
        primary key,
    parent_device_serial varchar(40) null comment '부모단말기시리얼',
    create_at timestamp not null comment '최초생성일시',
    create_user_idx int not null comment '최초생성사용자인덱스',
    update_at timestamp null comment '최종변경일시',
    update_user_idx int null comment '최종변경사용자인덱스',
    del_yn char not null comment '삭제여부',
    device_info_idx int null comment '단말기정보인덱스',
    device_name varchar(50) not null comment '단말기명',
    device_vendor_idx int null comment '단말기제조사인덱스',
    device_vendor_name varchar(45) null comment '단말기제조사명',
    device_type_idx int not null comment '단말기타입인덱스',
    device_type_name varchar(45) not null comment '단말기타입명',
    device_category_idx int not null comment '단말기종류인덱스',
    device_category_name varchar(45) not null comment '단말기종류명',
    device_comm_type_idx int null comment '단말기통신타입인덱스',
    device_comm_type_name varchar(45) null comment '단말기통신타입명',
    device_ip_address varchar(128) null comment '단말기아이피',
    device_port int null comment '단말기포트',
    device_id varchar(50) null comment '단말기아이디',
    device_password varchar(128) null comment '단말기비밀번호',
    device_mac_address varchar(17) null comment '단말기MAC주소',
    `option` json null
)
    comment '단말기' charset=utf8;

create table if not exists area_devices
(
    area_idx int not null comment '구역인덱스',
    device_serial varchar(36) not null comment '단말기시리얼',
    constraint FK_area_devices1
        foreign key (area_idx) references areas (area_idx)
            on update cascade on delete cascade,
    constraint FK_area_devices2
        foreign key (device_serial) references devices (device_serial)
)
    comment '구역별단말기' charset=utf8;

create index FK_area_devices1_idx
    on area_devices (area_idx);

create index FK_area_devices2_idx
    on area_devices (device_serial);

create table if not exists group_roles
(
    group_role_idx int not null comment '그룹역할인덱스'
        primary key,
    group_role_name varchar(45) null comment '그룹역할명'
)
    comment '그룹역할' charset=utf8;

create table if not exists kuf_did_categories
(
    area_idx int not null,
    category varchar(40) not null,
    duration int default 0 not null,
    primary key (area_idx, category)
)
    charset=utf8;

create table if not exists kuf_did_media
(
    area_idx int not null,
    category varchar(40) not null,
    media_order varchar(40) not null,
    media_type varchar(10) not null,
    contents text null,
    primary key (area_idx, category, media_order)
)
    charset=utf8;

create table if not exists layout_templates
(
    template_idx int auto_increment comment '템플릿인덱스'
        primary key,
    template_name varchar(45) null comment '템플릿명',
    template_data text null comment '템플릿데이터'
)
    comment '레이아웃템플릿' charset=utf8;

create table if not exists login_device_types
(
    login_device_type_idx int auto_increment comment '접속단말기타입인덱스'
        primary key,
    login_device_type_name varchar(45) null comment '접속단말기타입명'
)
    comment '접속단말기타입' charset=utf8;

create table if not exists notifications
(
    notification_idx int auto_increment comment '알림인덱스'
        primary key,
    notification_at timestamp(6) not null comment '알림시각',
    device_serial varchar(40) not null comment '단말기시리얼',
    device_name varchar(50) null comment '센서키',
    critical_level varchar(100) not null comment '임계치',
    message text not null comment '알림메시지',
    notification_value text null comment '값',
    constraint FK_notifications1
        foreign key (device_serial) references devices (device_serial)
            on update cascade on delete cascade
)
    comment '알림' charset=utf8;

create index FK_notifications1_idx
    on notifications (device_serial);

create table if not exists patterns
(
    pattern_idx int auto_increment comment '패턴인덱스'
        primary key,
    pattern_name varchar(200) not null comment '패턴명',
    work_time int null comment '작동시간(분)',
    sleep_time int null comment '휴식시간(분)'
)
    comment '패턴' charset=utf8;

create table if not exists rule_prototypes
(
    rule_id varchar(40) not null comment '룰인덱스'
        primary key,
    area_idx int not null comment '구역인덱스',
    create_at timestamp not null comment '최초생성일시',
    create_user_idx int not null comment '최초생성사용자인덱스',
    update_at timestamp null comment '최종변경일시',
    update_user_idx int null comment '최종변경사용자인덱스',
    del_yn char not null comment '삭제여부',
    rule_name varchar(100) null comment '룰명',
    rule_condition json null comment '조건정의',
    action_message text null comment '동작메시지',
    opposition_yn char default 'N' null,
    opposition_message text null
)
    comment '룰프로토타입' charset=utf8;

create table if not exists device_rules
(
    device_serial varchar(40) not null comment '단말기시리얼',
    rule_id varchar(40) not null comment '룰인덱스',
    auto_yn char default 'N' not null comment '자동/수동 여부 (Y/N)',
    primary key (device_serial, rule_id),
    constraint FK_device_rules1
        foreign key (device_serial) references devices (device_serial)
            on update cascade on delete cascade,
    constraint FK_device_rules2
        foreign key (rule_id) references rule_prototypes (rule_id)
            on update cascade on delete cascade
)
    comment '단말기별룰' charset=utf8;

create table if not exists rules
(
    rule_idx int auto_increment comment '룰인덱스'
        primary key,
    create_at timestamp not null comment '최초생성일시',
    create_user_idx int not null comment '최초생성사용자인덱스',
    update_at timestamp null comment '최종변경일시',
    update_user_idx int null comment '최종변경사용자인덱스',
    del_yn char not null comment '삭제여부',
    rule_name varchar(100) null comment '룰명',
    critical_level varchar(100) null comment '임계치레벨',
    control_data json null comment '제어정보'
)
    comment '룰' charset=utf8;

create table if not exists rule_condition_rels
(
    rule_idx int not null comment '룰인덱스',
    condition_idx int not null comment '조건인덱스',
    sort_order int null comment '정렬순서',
    expire_at int null comment '만료시간',
    constraint FK_rule_condition_rels1
        foreign key (condition_idx) references conditions (condition_idx),
    constraint FK_rule_condition_rels2
        foreign key (rule_idx) references rules (rule_idx)
)
    comment '룰조건관계' charset=utf8;

create index FK_rule_condition_rels1_idx
    on rule_condition_rels (condition_idx);

create index FK_rule_condition_rels2_idx
    on rule_condition_rels (rule_idx);

create table if not exists schedule_cycle
(
    schedule_cycle_idx int not null comment '스케줄주기인덱스'
        primary key,
    device_serial varchar(40) not null comment '단말기시리얼',
    start_at timestamp(6) null comment '시작시간',
    end_at timestamp(6) null comment '종료시간',
    term int null comment '주기',
    message text null comment '메시지',
    constraint FK_schedule_cycle1
        foreign key (device_serial) references devices (device_serial)
            on update cascade on delete cascade
)
    comment '스케줄주기' charset=utf8;

create index schedule_cycle_index1
    on schedule_cycle (device_serial);

create table if not exists schedule_time
(
    schedule_time_idx int not null comment '스케줄시간인덱스'
        primary key,
    device_serial varchar(40) not null comment '단말기시리얼',
    time_array text null comment '시간목록',
    message text null comment '메시지',
    constraint FK_schedule_time1
        foreign key (device_serial) references devices (device_serial)
            on update cascade on delete cascade
)
    comment '스케줄시간' charset=utf8;

create index schedule_time_index1
    on schedule_time (device_serial);

create table if not exists skyfarm_categories
(
    skyfarm_idx int not null,
    category varchar(40) not null,
    duration int default 0 not null,
    primary key (skyfarm_idx, category)
)
    charset=utf8;

create table if not exists skyfarm_media
(
    skyfarm_idx int not null,
    category varchar(40) not null,
    media_order varchar(40) not null,
    tab_title varchar(200) not null,
    media_type varchar(10) not null,
    contents text null,
    primary key (skyfarm_idx, category, tab_title, media_order)
)
    charset=utf8;

create table if not exists user_groups
(
    user_group_idx int auto_increment comment '사용자그룹인덱스'
        primary key,
    create_at timestamp not null comment '최초생성일시',
    create_user_idx int not null comment '최초생성사용자인덱스',
    update_at timestamp null comment '최종변경일시',
    update_user_idx int null comment '최종변경사용자인덱스',
    del_yn char not null comment '삭제여부',
    user_group_name varchar(45) not null comment '사용자그룹명',
    user_group_phone varchar(14) null comment '사용자그룹전화번호',
    user_group_address text null comment '사용자그룹주소'
)
    comment '사용자그룹' charset=utf8;

create table if not exists users
(
    user_idx int auto_increment comment '사용자인덱스'
        primary key,
    create_at timestamp not null comment '최초생성일시',
    create_user_idx int not null comment '최초생성사용자인덱스',
    update_at timestamp null comment '최종변경일시',
    update_user_idx int null comment '최종변경사용자인덱스',
    del_yn char not null comment '삭제여부',
    user_id varchar(50) not null comment '사용자아이디',
    user_password varchar(128) not null comment '사용자비밀번호',
    password_salt varchar(64) not null comment '패스워드암호화SALT',
    user_name varchar(45) not null comment '사용자명',
    user_email varchar(100) not null comment '사용자이메일',
    user_email_auth_yn char not null comment '사용자이메일인증여부',
    user_phone varchar(14) null comment '사용자전화번호',
    user_address text null comment '사용자주소',
    constraint user_id_UNIQUE
        unique (user_id)
)
    comment '사용자' charset=utf8;

create table if not exists group_members
(
    user_group_idx int not null comment '사용자그룹인덱스',
    user_idx int not null comment '사용자인덱스',
    group_role_idx int not null comment '그룹역할인덱스',
    create_at timestamp not null comment '최초생성일시',
    create_user_idx int not null comment '최초생성사용자인덱스',
    update_at timestamp null comment '최종변경일시',
    update_user_idx int null comment '최종변경사용자인덱스',
    del_yn char not null comment '삭제여부',
    constraint FK_group_members1
        foreign key (user_group_idx) references user_groups (user_group_idx),
    constraint FK_group_members2
        foreign key (user_idx) references users (user_idx),
    constraint FK_group_members3
        foreign key (group_role_idx) references group_roles (group_role_idx)
)
    comment '사용자그룹맴버' charset=utf8;

create index FK_group_members1_idx
    on group_members (user_group_idx);

create index FK_group_members2_idx
    on group_members (user_idx);

create index FK_group_members3_idx
    on group_members (group_role_idx);

create table if not exists login_devices
(
    login_device_idx int auto_increment comment '접속단말기인덱스'
        primary key,
    create_at timestamp not null comment '최초생성일시',
    create_user_idx int not null comment '최초생성사용자인덱스',
    update_at timestamp null comment '최종변경일시',
    update_user_idx int null comment '최종변경사용자인덱스',
    del_yn char not null comment '삭제여부',
    user_idx int not null comment '사용자인덱스',
    login_device_uuid varchar(45) null comment '접속단말기UUID',
    login_device_name varchar(45) not null comment '접속단말기명',
    login_device_type_idx int not null comment '접속단말기타입인덱스',
    login_device_os varchar(45) null comment '단말기OS',
    push_token text null comment '푸시토큰',
    login_token text null comment '로그인토큰',
    constraint FK_login_devices1
        foreign key (login_device_type_idx) references login_device_types (login_device_type_idx),
    constraint FK_login_devices2
        foreign key (user_idx) references users (user_idx)
)
    comment '접속단말기' charset=utf8;

create table if not exists user_areas
(
    user_idx int not null comment '사용자인덱스',
    area_idx int not null comment '구역인덱스',
    area_name varchar(45) null,
    area_role_idx int not null comment '구역역할인덱스',
    area_role_name varchar(45) null,
    primary key (user_idx, area_idx),
    constraint FK_user_areas1
        foreign key (user_idx) references users (user_idx),
    constraint FK_user_areas2
        foreign key (area_role_idx) references area_roles (area_role_idx)
)
    comment '사용자구역' charset=utf8;

create table if not exists video_files
(
    video_file_idx int auto_increment
        primary key,
    area_idx int not null,
    device_serial varchar(40) not null,
    record_at timestamp(6) not null,
    video_type int not null,
    video_length int not null,
    video_path text not null,
    thumb_path text not null
)
    comment '비디오파일 목록' charset=utf8;

create table if not exists widget_templates
(
    widget_template_idx int auto_increment comment '위젯템플릿인덱스'
        primary key,
    width int not null comment '가로길이',
    height int not null comment '세로길이',
    widget_name text not null comment '위젯명',
    data json null comment '데이터'
)
    comment '위젯템플릿' charset=utf8;

create table if not exists widgets
(
    area_idx int not null comment '구역인덱스',
    widget_template_idx int not null comment '위젯템플릿인덱스',
    dashboard_page int not null comment '페이지',
    x int not null comment '가로시오프셋',
    y int not null comment '세로오프셋',
    width int not null comment '가로길이',
    height int not null comment '세로길이',
    widget_name text not null comment '위젯명',
    widget_data json not null comment '데이터',
    primary key (area_idx, widget_template_idx, dashboard_page, x, y)
)
    comment '위젯' charset=utf8;

