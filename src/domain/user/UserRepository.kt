package com.example.domain.user

/**
 * 사용자 저장소 포트 (Port)
 * - 도메인 레이어에서 정의하는 인터페이스
 * - 구현체는 Infrastructure 레이어에서 제공 (Adapter)
 * - 도메인 로직이 인프라스트럭처에 의존하지 않도록 보장
 */
interface UserRepository {
    
    /**
     * 사용자 저장
     * @param user 저장할 사용자 엔티티
     * @return 저장된 사용자 엔티티 (ID가 생성된 상태)
     */
    suspend fun save(user: User): User
    
    /**
     * 사용자 ID로 조회
     * @param id 사용자 ID
     * @return 사용자 엔티티 또는 null (존재하지 않는 경우)
     */
    suspend fun findById(id: UserId): User?
    
    /**
     * 이메일로 사용자 조회
     * @param email 사용자 이메일
     * @return 사용자 엔티티 또는 null (존재하지 않는 경우)
     */
    suspend fun findByEmail(email: Email): User?
    
    /**
     * 사용자 삭제
     * @param id 삭제할 사용자 ID
     */
    suspend fun delete(id: UserId)
    
    /**
     * 이메일 중복 확인
     * @param email 확인할 이메일
     * @return 중복 여부 (true: 이미 존재, false: 사용 가능)
     */
    suspend fun existsByEmail(email: Email): Boolean
    
    /**
     * 모든 활성 사용자 조회
     * @return 활성 사용자 목록
     */
    suspend fun findAllActiveUsers(): List<User>
    
    /**
     * 특정 기간 이후 생성된 사용자 수 조회
     * @param fromDate 기준 일시
     * @return 사용자 수
     */
    suspend fun countUsersCreatedAfter(fromDate: java.time.LocalDateTime): Long
}
