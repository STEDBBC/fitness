-- Удаляем старую внешнюю связь на employees(id)
ALTER TABLE schedules
DROP FOREIGN KEY schedules_ibfk_2;

-- Добавляем новую внешнюю связь на users(id)
ALTER TABLE schedules
    ADD CONSTRAINT fk_schedule_trainer
        FOREIGN KEY (trainer_id)
            REFERENCES users(id);
