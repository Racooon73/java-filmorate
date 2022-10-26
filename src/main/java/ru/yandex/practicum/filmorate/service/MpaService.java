package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
public class MpaService {
    private final MpaDao mpaDao;

    public MpaService(MpaDao mpaDao){

        this.mpaDao = mpaDao;
    }
    public List<Mpa> getMpa() {
        return mpaDao.getMpa();
    }

    public Mpa getMpaById(int id) throws NotFoundException {
        return mpaDao.getMpaById(id);
    }
}
