package com.checkout.domain.repository

import com.checkout.domain.model.Deal
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DealRepository: CrudRepository<Deal, Long> {

}