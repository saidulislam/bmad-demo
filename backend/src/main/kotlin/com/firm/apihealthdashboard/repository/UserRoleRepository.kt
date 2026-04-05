package com.firm.apihealthdashboard.repository

import com.firm.apihealthdashboard.model.entity.UserRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRoleRepository : JpaRepository<UserRole, UUID> {
    fun findByUsername(username: String): UserRole?
    fun findByRole(role: String): List<UserRole>
}
