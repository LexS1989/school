-- liquibase formatted sql

-- changeSet Shadrin:1
CREATE INDEX student_name_index ON student (name)

-- changeSet Shadrin:2
CREATE INDEX faculty_name_color_index ON faculty (name, color)