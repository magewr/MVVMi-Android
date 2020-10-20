package com.magewr.mvvmi.interactors.favoriteusers

import com.magewr.mvvmi.ui.main.model.Users
import io.reactivex.rxjava3.subjects.PublishSubject

interface FavoriteUsersInteractorInterface {
    fun toggleFavoriteUser(user: Users)
}

class FavoriteUsersInteractor : FavoriteUsersInteractorInterface {
    val subject: PublishSubject<Unit> = PublishSubject.create()

    override fun toggleFavoriteUser(user: Users) {
        val id = user.id
        if (LocalFavoriteUsers.users[id] != null)
            LocalFavoriteUsers.users.remove(id)
        else
            LocalFavoriteUsers.users[id] = user

        subject.onNext(Unit)
    }

}

object LocalFavoriteUsers {
    val users: HashMap<Long, Users> = HashMap()
}