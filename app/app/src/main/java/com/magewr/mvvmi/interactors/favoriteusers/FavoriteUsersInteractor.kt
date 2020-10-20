package com.magewr.mvvmi.interactors.favoriteusers

import com.magewr.mvvmi.ui.main.model.Users
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

interface FavoriteUsersInteractorInterface {
    fun toggleFavoriteUser(user: Users)
    val subject: Subject<Unit>
}

// 좋아요 인터렉터
class FavoriteUsersInteractor : FavoriteUsersInteractorInterface {
    override val subject: PublishSubject<Unit> = PublishSubject.create()

    override fun toggleFavoriteUser(user: Users) {
        val id = user.id
        if (LocalFavoriteUsers.users[id] != null)
            LocalFavoriteUsers.users.remove(id)
        else
            LocalFavoriteUsers.users[id] = user

        subject.onNext(Unit)
    }

}


// 로컬 좋아요 정보 저장용 오브젝트 (Local DB 대신 사용)
object LocalFavoriteUsers {
    val users: HashMap<Long, Users> = HashMap()
}