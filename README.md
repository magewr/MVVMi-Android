# MVVMi-Android
Android, iOS 통합 아키텍쳐 샘플입니다.

Clean Architecture 기반의 MVVM + Interactor 로 구성하였습니다.

Android, iOS 통합 아키텍쳐로 Model, ViewModel, Interactor 레이어는 언어만 다를 뿐 동일한 로직을 사용하는 것이 목표입니다.

AAC의 ViewModel은 사용하지 않습니다. 여기서의 ViewModel은 안드로이드 플랫폼에 종속되지 않습니다.

##

View 레이어는 각각의 플랫폼에 종속됩니다.

View 레이어를 제외한 각각의 레이어는 Interface로 참조됩니다.

Android 코드에서는 Mockito를 이용하여 각각의 레이어에서 UnitTest가 이루어집니다.

현재 ViewModel, Interactor 레이어에 테스트가 구현되어 있습니다.

##

코드는 지속 변경될 수 있습니다.

실제 활용한 코드는 브랜치에서 확인할 수 있습니다. - 추후 정리되면 따로 레파짓으로 분리예정입니다.

iOS Code : https://github.com/magewr/MVVMi-iOS
