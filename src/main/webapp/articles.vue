<style scoped>

.searching {
  animation-name: appear;
  animation-duration: 2s;
}

.innerDiv {
  animation-name: appear;
  animation-duration: 2s;
}

@keyframes appear {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.ellipsis {
  width: 700px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}


.search-result .title h3 {
  margin: 0 0 15px;
  color: #333;
}

.search-result .title p {
  font-size: 12px;
  color: #333;
}

.well {
  border: 0;
  padding: 20px;
  min-height: 63px;
  background: #fff;
  box-shadow: none;
  border-radius: 3px;
  position: relative;
  max-height: 100000px;
  border-bottom: 2px solid #ccc;
  transition: max-height 0.5s ease;
  -o-transition: max-height 0.5s ease;
  -ms-transition: max-height 0.5s ease;
  -moz-transition: max-height 0.5s ease;
  -webkit-transition: max-height 0.5s ease;
}

.form-control {
  height: 45px;
  padding: 10px;
  font-size: 16px;
  box-shadow: none;
  border-radius: 0;
  position: relative;
}

.inner_row {
  padding: 10px;
  margin: 3px;
  background-color: #f1ecec;
}

.inner_row:hover {
  opacity: 0.8;
  cursor: pointer;
}

.button_wrap {
  margin: 5px;
  justify-content: center;
  display: flex;
}

.button_wrap button {
  margin-right: 20px;
  margin-left: 20px;
}


</style>

<template>
  <div class="container bootstrap snippets bootdey " style="height: 100vh">
    <div class="row">
      <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
        <div class="well search-result">
          <div class="input-group">
            <input type="text" class="form-control" placeholder="Search" @change="onChange()" @focus="onFocus"
                   @blur="onBlur"
                   v-model="data.keyword">
            <span class="input-group-btn">
              <button class="btn  btn-lg btn-primary" type="button" @click="search">
                        <i class="glyphicon glyphicon-search"></i>
                        Search
                    </button>
            </span>
          </div>
          <div>
            <h3 class="searching" v-if="data.input">What are you searching for? Is it...</h3>
          </div>
        </div>

        <div class="well search-result " style="overflow: auto; height: 75vh">
          <div class="button_wrap">
            <button type="button" class="btn btn-success  btn-block" @click="newarticle">
              <i class="glyphicon glyphicon-refresh"></i>New Article
            </button>
            <button type="button" class="btn btn-success  btn-block" @click="getarticles">
              <i class="glyphicon glyphicon-refresh"></i>Show All Article
            </button>
          </div>

          <div class="row inner_row" v-for="article in data.articledata">
            <div class="col-xs-6 col-sm-9 col-md-9 col-lg-10 title">
              <div class="innerDiv">
                <div @click="goto(article.articleId)">
                  <h3>{{ article.articleTitle }}</h3>
                  <p class="ellipsis">{{ article.articleContent }}</p>
                </div>

                <button type="button" class="btn btn-danger  btn-block" @click="deleteArticle(article.articleId)">Delete
                </button>
              </div>

            </div>
          </div>
        </div>
<!--        <div class="row">-->
<!--          <button type="button" class="btn btn-primary  btn-block">-->
<!--            <i class="glyphicon glyphicon-refresh"></i>Load more...-->
<!--          </button>-->
<!--        </div>-->
      </div>
    </div>
  </div>
  <!--  </div>-->
</template>

<script setup>
import {reactive, onMounted, watchEffect} from 'vue'


const data = reactive({
  articledata: [],
  input: false,
  keyword: '',
  user: {}
})

function onBlur() {
  data.input = false;

}

function onChange() {
  if (data.input) {
    search(data.keyword)
  } else {
    clearInterval(repeat);
  }

}

let repeat;

function onFocus() {
  data.input = true;
  repeat = setInterval(() =>
      onChange(), 2000
  )

}

function getuserdata() {
  fetch('/markdownblog/api/auth/userdata', {
    method: 'get',
    headers: {
      'Content-Type': 'application/json'
    },
  }).then(response => {
    return response.json()
  }).then(d => {
    data.user = d;
    console.log(d.login)
  })
}

function newarticle() {
  if (data.user.login) {
    router.push("/editor")
  } else {
    alert('你沒登入🙄')
    router.push('/')
  }

}


function deleteArticle(articleId) {
  if (confirm('確定要刪除嗎？')) {
    fetch('/markdownblog/api/markdown/delete', {
      method: 'post',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        articleId: articleId,
      })
    })
        .then(response => {
          if (response.status === 200)
            console.log("article deleted");
            getarticles();
        })
  }

}

function getarticles() {
  fetch('/markdownblog/api/markdown/articles', {
    method: 'get',
    headers: {
      'Content-Type': 'application/json'
    },
  }).then(response => {
    return response.json()
  }).then(d => {
    // data.user.userId = d.userId;
    data.articledata = d;
    console.log(d);

  })
}

function search() {
  fetch('/markdownblog/api/markdown/search/' + data.keyword, {
    method: 'get',
    headers: {
      'Content-Type': 'application/json; charset=utf-8'
    },
  })
      .then(response => {
        return response.json()
      }).then(d => {
    // data.user.userId = d.userId;
    data.articledata = d;
    console.log(d);
  })

}

function goto(articleId) {
  // router.push({ path:'/markdownblog/editor',params: {articleId}})
  router.push('/editor/' + articleId)
}

onMounted(() => {
  getarticles();
  getuserdata();
})


</script>