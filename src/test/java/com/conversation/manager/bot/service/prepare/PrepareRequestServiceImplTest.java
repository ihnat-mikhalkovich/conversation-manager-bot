package com.conversation.manager.bot.service.prepare;

import com.conversation.manager.bot.entity.Group;
import com.conversation.manager.bot.service.request.TelegramRequestService;
import com.conversation.manager.bot.telegram.method.GetChatWithEquals;
import com.conversation.manager.bot.telegram.method.SaveGetChatMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.function.SupplierUtils;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PrepareRequestServiceImplTest {

    @Autowired
    private PrepareRequestService prepareRequestService;

    @MockBean
    private TelegramRequestService telegramRequestService;

    @BeforeEach
    public void setUpCheckChatMemberService() {
        final SaveGetChatMember saveGetChatMember1 = new SaveGetChatMember();
        saveGetChatMember1.setUserId(101);
        saveGetChatMember1.setChatId(201L);
        when(telegramRequestService.existChatMember(saveGetChatMember1)).thenReturn(true);

        final SaveGetChatMember saveGetChatMember2 = new SaveGetChatMember();
        saveGetChatMember2.setUserId(101);
        saveGetChatMember2.setChatId(202L);
        when(telegramRequestService.existChatMember(saveGetChatMember2)).thenReturn(false);

        final SaveGetChatMember saveGetChatMember3 = new SaveGetChatMember();
        saveGetChatMember3.setUserId(101);
        saveGetChatMember3.setChatId(-1001246979812L);
        when(telegramRequestService.existChatMember(saveGetChatMember3)).thenReturn(false);

        final SaveGetChatMember saveGetChatMember4 = new SaveGetChatMember();
        saveGetChatMember4.setUserId(102);
        saveGetChatMember4.setChatId(201L);
        when(telegramRequestService.existChatMember(saveGetChatMember4)).thenReturn(true);

        final SaveGetChatMember saveGetChatMember5 = new SaveGetChatMember();
        saveGetChatMember5.setUserId(102);
        saveGetChatMember5.setChatId(202L);
        when(telegramRequestService.existChatMember(saveGetChatMember5)).thenReturn(true);

        final SaveGetChatMember saveGetChatMember6 = new SaveGetChatMember();
        saveGetChatMember6.setUserId(102);
        saveGetChatMember6.setChatId(-1001246979812L);
        when(telegramRequestService.existChatMember(saveGetChatMember6)).thenReturn(false);
    }

    @Test
    public void findOneGroup() {
        final int userId = 101;
        final Set<Group> expectedGroups = new HashSet<>();
        expectedGroups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setGroupId(201L);
            return group;
        }));

        final Set<Group> resultGroups = prepareRequestService.findGroupsByUserId(userId);

        assertEquals(expectedGroups, resultGroups);
    }

    @Test
    public void findTwoGroups() {
        final int userId = 102;
        final Set<Group> expectedGroups = new HashSet<>();
        expectedGroups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setGroupId(201L);
            return group;
        }));
        expectedGroups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setGroupId(202L);
            return group;
        }));

        final Set<Group> resultGroups = prepareRequestService.findGroupsByUserId(userId);

        assertEquals(expectedGroups, resultGroups);
    }

    @Test
    public void testFindChats() {
        final TelegramRequestService mockSend = mock(TelegramRequestService.class);

        final GetChat getChat1 = new GetChatWithEquals();
        getChat1.setChatId(201L);
        final Chat chat1 = mock(Chat.class);
        when(chat1.getId()).thenReturn(201L);
        when(mockSend.getChat(getChat1)).thenReturn(Optional.of(chat1));

        final GetChat getChat2 = new GetChatWithEquals();
        getChat2.setChatId(202L);
        final Chat chat2 = mock(Chat.class);
        when(chat2.getId()).thenReturn(202L);
        when(mockSend.getChat(getChat2)).thenReturn(Optional.of(chat2));

        final GetChat getChat3 = new GetChatWithEquals();
        getChat3.setChatId(203L);
        when(mockSend.getChat(getChat3)).thenReturn(Optional.empty());

        final Set<Group> groups = new HashSet<>();
        groups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setGroupId(201L);
            return group;
        }));
        groups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setGroupId(202L);
            return group;
        }));
        groups.add(SupplierUtils.resolve(() -> {
            final Group group = new Group();
            group.setGroupId(203L);
            return group;
        }));

        final PrepareRequestService prepareRequestService = new PrepareRequestServiceImpl(mockSend, null);
        final Set<Chat> chats = prepareRequestService.findChats(groups);

        final Set<Chat> expectedChats = new HashSet<>();
        expectedChats.add(chat1);
        expectedChats.add(chat2);

        assertEquals(expectedChats, chats);
    }
}