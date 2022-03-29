package com.unina.natourkt.core.domain.use_case.chat

import com.unina.natourkt.core.domain.model.Chat
import com.unina.natourkt.core.domain.model.User
import com.unina.natourkt.core.domain.repository.ChatRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetChatByMembersUseCaseTest {

    // Class to Test
    @InjectMocks
    private lateinit var getChatByMembers: GetChatByMembersUseCase

    // Mocked dependency
    @Mock
    private lateinit var repository: ChatRepository

    // Utilities
    private lateinit var firstMember: User
    private lateinit var secondMember: User
    private lateinit var brokenUser: User
    private lateinit var dummyChat: Chat

    @Before
    fun setUp() {
        getChatByMembers = GetChatByMembersUseCase(repository)
    }

    @Test
    fun `when the given IDs are minus or equal to zero, it should return NotAcceptable as last flow value`() =
        runTest {
            firstMember = User(-1, "marietto", false, "")
            secondMember = User(2, "bianca", true, "")

            val result = getChatByMembers(firstMember.id, secondMember.id).last()
            assertThat(result.error, equalTo(DataState.Cause.NotAcceptable))
        }

    @Test
    fun `when the given IDs are equal, it should return BadRequest as last flow value`() =
        runTest {
            firstMember = User(2, "marietto", false, "")
            secondMember = firstMember

            val result = getChatByMembers(firstMember.id, secondMember.id).last()
            assertThat(result.error, equalTo(DataState.Cause.BadRequest))
        }

    @Test
    fun `when the IDs are good to go, the result data should be a Chat entity`() {
        runTest {
            firstMember = User(2, "marietto", false, "")
            secondMember = User(3, "mattia", isAdmin = false, "")
            dummyChat = Chat(34, LocalDate.now(), firstMember, secondMember)

            val request = repository.getChatByMembers(firstMember.id, secondMember.id)
            whenever(request).thenReturn(DataState.Success(dummyChat))

            val result = getChatByMembers(firstMember.id, secondMember.id).last()

            verify(repository).getChatByMembers(firstMember.id, secondMember.id)
            assertThat(result.data, equalTo(dummyChat))
        }
    }

    @Test
    fun `when the IDs are good to go but the chat is not found, the result error should be a NotFound`() {
        runTest {
            firstMember = User(2, "marietto", false, "")
            secondMember = User(3, "mattia", false, "")
            brokenUser = User(5, "bianca", true, "")
            dummyChat = Chat(34, LocalDate.now(), firstMember, brokenUser)

            val request = repository.getChatByMembers(firstMember.id, secondMember.id)
            whenever(request).thenReturn(DataState.Success(dummyChat))

            val result = getChatByMembers(firstMember.id, secondMember.id).last()

            verify(repository).getChatByMembers(firstMember.id, secondMember.id)
            assertThat(result.error, equalTo(DataState.Cause.NotFound))
        }
    }
}