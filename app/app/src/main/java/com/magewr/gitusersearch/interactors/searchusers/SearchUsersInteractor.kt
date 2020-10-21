package com.magewr.gitusersearch.interactors.searchusers

import com.magewr.gitusersearch.clients.ClientInterface
import com.magewr.gitusersearch.clients.apis.APISearch
import com.magewr.gitusersearch.interactors.favoriteusers.LocalFavoriteUsers
import com.magewr.gitusersearch.ui.main.model.SearchUsersResultModel
import io.reactivex.rxjava3.core.Single

// 인터렉터는 인터페이스로 되어있으며 실제 콘크리트 클래스는 이 인터페이스를 구현하도록 함
// 뷰모델에서는 이 인터페이스를 레퍼런스로 가지고 있기 때문에 실제 콘크리트 클래스가 무엇인지와 상관없이 동작하게 설계
interface SearchUsersInteractorInterface {

    // 유저 검색 메소드
    fun getSearchUsers(param: SearchUsersParam): Single<SearchUsersResultModel>

    // 좋아요 상태 변경시 핸들 메소드
    fun favoriteUpdated() : Single<SearchUsersResultModel>
}

// API 호출용 인터렉터
class SearchUsersInteractor(private val client: ClientInterface<APISearch>) :
    SearchUsersInteractorInterface {

    // 실제 API 호출하여 서버로부터 쿼리에 해당하는 유저 목록 받아옴
    override fun getSearchUsers(param: SearchUsersParam): Single<SearchUsersResultModel> {
        if (param.q.isBlank())
            return Single.never()

        return client.api.getSearchUsers(param.q, param.sort, param.order, param.page, param.per_page)
    }

    // API 인터렉터에서는 좋아요 상태 변경시에 딱히 핸들링 할 내용이 없어서 무시
    override fun favoriteUpdated(): Single<SearchUsersResultModel> {
        return Single.never()
    }
}

// 로컬 호출용 인터렉터
class LocalUsersInteractor :
    SearchUsersInteractorInterface {

    private var latestParam =
        SearchUsersParam(
            "",
            null,
            null,
            1,
            10
        )

    // 로컬 좋아요 인터렉터에서는 쿼리에 해당하는 좋아요 목록을 받아옴. 여기서는 오브젝트를 사용하지만 로컬디비를 사용한다면 오브젝트 대신 디비 사용하도록 변경
    override fun getSearchUsers(param: SearchUsersParam): Single<SearchUsersResultModel> {
        val users = LocalFavoriteUsers.users.values.toList().filter { it.login.contains(param.q, true) }
        val count = users.size.toLong()
        val incompleteResults = false
        val resultModel =
            SearchUsersResultModel(
                count,
                incompleteResults,
                users
            )

        latestParam = param

        return Single.just(resultModel)
    }

    // 좋아요 상태 변경 시 로컬 목록이 갱신되어야 하므로 유저목록을 다시 요청함
    override fun favoriteUpdated(): Single<SearchUsersResultModel> {
        return getSearchUsers(latestParam)
    }

}