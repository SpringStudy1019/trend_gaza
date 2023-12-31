<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { detailReview, deleteReview } from '@/api/review';
import { getAttractionDetail } from '@/api/attraction';
import { listComment } from '@/api/comment';
import { onFollow, offFollow } from '@/api/follow';
import { useUserStore } from '@/stores/user';
import ReviewCommentList from './ReviewCommentList.vue';
import ReviewImage from './item/ReviewImage.vue';

const route = useRoute();
const router = useRouter();
const store = useUserStore();

const { reviewIdx } = route.params;

const review = ref({});
const comments = ref([]);
const commentNumber = ref(0);

onMounted(() => {
  getReview();
  getComments();
});

// 리뷰 번호로 리뷰 얻어오기
const attractionName = ref('')
const getReview = () => {
  detailReview(reviewIdx,
    ({ data }) => {
      review.value = data; 
      followingInfo.value.followerId = data.userId;
      // 여행지 제목 review.contentId
      getAttractionDetail(
        review.value.contentId,
        ({ data }) => { 
          attractionName.value = data.title;
          console.log(attractionName.value);
          },
          (error) => {
            console.log(error);
          }
  );
    },
    (error) => {
      console.log(error);
      // Handle any errors here if needed
    }
  );
};

// 리뷰 댓글 얻어오기
const getComments = () => {
  listComment(reviewIdx,
    ({ data }) => { 
      comments.value = data;
     // number of comments
     commentNumber.value = comments.value.length;
    },
    (error) => {
      console.log(error);
    }
  );
};

// 댓글 개수
const handleCommentChanged = (commentLength) => {
    // Update the commentNumber or any other logic based on the commentData
    console.log("Comment changed:", commentLength.value);
    commentNumber.value = commentLength
};

function moveList() {
  router.push({ name: "review-list" });
}

function moveModify() {
  router.push({ name: "review-modify", params: { reviewIdx:reviewIdx } });
}

function onDeleteArticle() {
  if(!isDelete()) {
    return;
  }
  const { reviewIdx } = route.params;
   // API 호출
   deleteReview(reviewIdx,
    ({data}) => {
      router.push({ name: "review-list" });
    }, (error) => {
      console.log(error);
    })
}

function isDelete() {
  return confirm("정말 삭제하시겠습니까?");
}

// 팔로잉 등록, 취소
const followingInfo = ref({
  followeeId: store.userInfo.userId, 
  followerId: ""
})

// 다른 사용자 페이지 가기
const goYourPage = (userId) => {
  if (store.userInfo.userId != userId) {
    router.push(`/${userId}`);
  }
}
</script>

<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-lg-10">
        <!-- <h2 class="my-3 py-3 shadow-sm bg-light text-center">
          <mark class="sky">리뷰보기</mark>
        </h2> -->
      </div>
      <div class="col-lg-10 text-start">
        <div class="row my-2">
          <h2 class="text-bold px-2">{{ review.title }}</h2>
        </div>
        <div class="row my-2">
          <h3 id="attractionId">[{{ attractionName }}]에 대한 후기입니다 </h3>
        </div>
        <router-link 
            v-if="review.contentId && Number.isInteger(review.contentId)"
            :to="{ name: 'attraction-view', params: { attractionIdx: review.contentId } }" 
            class="btn btn-info">
                여행지 자세히 보기
        </router-link>
        <div class="row">
          <div class="col-md-8">
            <div class="clearfix align-content-center">
              <!-- 사용자 조회 router-link-->
              <div class='margin-small'></div>
              <button class="btn btn-light" @click="goYourPage(review.userId)" id='user-button'> {{ review.userId }}</button>
              <!-- <button id="follow-button" @click="toggleFollow" v-if="store.userInfo.userId !== review.userId">+ Follow</button>	 -->
              <div class='margin-small'></div>
              <div class="text-secondary fw-light">
                {{ review.registerDate }}
              </div>
              <div class='margin-small'></div>
              <p>
                <span class="text fw">
                  점수 : {{ review.score }}
                </span>
              </p>
            </div>
          </div>
          <div class="col-md-4 align-self-center text-end">댓글 : {{commentNumber}}</div>
          <div class='margin-small'></div>
          <div class="divider mb-3"></div>
          <div class="text">
            {{ review.content }}
          </div>
          <div >
            <!-- 이미지 불러오기 -->
            <review-image />
          </div>
          <div class="divider mt-3 mb-3"></div>
          <div class="d-flex justify-content-end">
            <button type="button" class="btn btn-outline-primary mb-3" @click="moveList">
              리뷰목록
            </button>
            <button type="button" class="btn btn-outline-success mb-3 ms-1" @click="moveModify">
              리뷰수정
            </button>
            <button type="button" class="btn btn-outline-danger mb-3 ms-1" @click="onDeleteArticle">
              리뷰삭제
            </button>
          </div>
          <!-- 리뷰 댓글 -->
          <ReviewCommentList :reviewIdx="parseInt(reviewIdx)" @commentChanged="handleCommentChanged" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

.img-group {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    border: black solid 1px;
    width: 10vh;
    margin: 20px;
}

.margin-small {
  margin-bottom: 20px;
}

#user-button:hover{
  color: white;
  background-color: black;
  border: none; 
  font-weight: bold;
}
</style>