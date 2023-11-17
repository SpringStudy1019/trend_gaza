<script setup>
import { ref, onMounted, defineProps } from "vue";
import { useRouter } from "vue-router";
import { useUserStore } from '@/stores/user';

import { listComment, deleteCommentRequest, registerCommentRequest } from "@/api/comment";

const props = defineProps({
  reviewIdx: Number, // Define the prop to accept reviewIdx as a Number
});

const comments = ref([]);
const commentContent = ref('');
 
const router = useRouter();
const store = useUserStore();

onMounted(() => {
    getCommentList();
    window.scrollTo({ top: 0, behavior: 'smooth' });
});

const getCommentList = () => {
    listComment(props.reviewIdx,
    (response) => {
      console.log("Success:", response.status, response.data);
      comments.value = response.data;
    },
    (error) => {
        console.log(error);
    }
  );
};

// delete
function deleteComment(commentIdx) {
    if(!isDelete()) {
    return;
  }
  deleteCommentRequest(commentIdx,
    ({data}) => {
        getCommentList();
      router.push({ name: "review-view" });
    }, (error) => {
      console.log(error);
    })
}

function isDelete() {
  return confirm("정말 삭제하시겠습니까?");
}

// register
function registerComment() {
    if (!commentContent.value) {
        const msg = "댓글을 입력하세요!"
        alert(msg)    
    return;
  }

  const newComment = {
    content: commentContent.value,
    registerDate: "",
    reviewIdx: props.reviewIdx,
    userId: store.userInfo.userId
    }
  
    registerCommentRequest(newComment,
        ({ data }) => {
            console.log(data);
            getCommentList();
            commentContent.value = '';
            router.push({name:"review-view"})
        },
        (error) => {
            console.log(error);
        });
}

// modify

</script>

<template>
   <h2 id="comment-title">이 리뷰에 대한 댓글</h2>
   <div class="comment-register">
    <div class="comment-regist">
    <div class="review-register-form">
        <input v-model="commentContent" placeholder="댓글을 입력하세요" />
        <button type="button" class="btn btn-outline-light mb-1 ms-3" @click="registerComment">
            등록</button>
        </div>
    </div>
    </div>  

    <div class="comment-list">
      <div v-for="comment in comments" :key="comment.commentIdx" class="comment">
        <div class="comment-content">{{ comment.content }}</div>
        <!-- <div class="comment-content" v-if="comment.commentIdx !== modifyingComment.commentIdx">
             {{ comment.content }}
         </div>
         <div class="comment-content" v-else>
             <input v-model="modifyingContent" />
         </div> -->
        <div class="comment-meta">
          <span class="comment-date">{{ comment.registerDate }}</span>
          <span class="comment-author">{{ comment.userId }}</span>
          <span class="comment-edit-date">{{ comment.modifyDate }}</span>
        </div>
        <div>
        <button type="button" class="btn btn-outline-primary mb-1 ms-3" @click="moveModify">
              수정
        </button>
        <button type="button" class="btn btn-outline-danger mb-1 ms-1" @click="() => deleteComment(comment.commentIdx)">
              삭제
        </button>
        </div>
      </div>
    </div>
    <div id='bottom'></div>
</template>

<style scoped>
#comment-title {
  margin-top: 50px; 
  margin-bottom: 30px; 
  font-size: 30px; 
  color: #ff9cbf;
}

#bottom {
    margin-bottom: 60px; 
}

.comment-register {
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-width: 500px;
  margin: 0 auto;
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-width: 500px;
  margin: 0 auto;
}

.comment-regist {
  background-color: #ff9cbf;
  padding: 10px;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.comment {
  background-color: #f7f7f7;
  padding: 10px;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.comment-content {
  font-size: 16px;
  line-height: 1.4;
}

.comment-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #888;
}

.comment-date {
  flex: 1;
}

.comment-author {
  flex: 1;
  text-align: center;
}

.comment-edit-date {
  flex: 1;
  text-align: right;
}
</style>