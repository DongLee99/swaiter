const express = require('express');
const multer = require('multer');
const path = require('path');
const fs = require('fs');
const { Post, Hashtag, User, Image } = require('../models');
const { isLoggedIn } = require('./middlewares');
const router = express.Router();
const Sequelize = require('sequelize')
const Op = Sequelize.Op;
const operatorsAliases = {
  $eq: Op.eq
}

router.post('/rank', async(req, res,next)=>{
  var big=0;
  var temp;
  var Content1 = new Array();
  const result = await Image.findAndCountAll()
        .then(result => {
          console.log(result.count);
          for (var i = 0; i<result.count; i++){
            
            for (var j = 0; j<result.count -i -1 ; j++){
              if(result.rows[j].cnt1 > result.rows[j+1].cnt1){
                temp = result.rows[j];
                console.log(temp);
                console.log(result.rows[j]);
                result.rows[j] = result.rows[j+1];
                result.rows[j+1] = temp;
              }
            }
          }
          for ( var i = result.count-1; i > result.count-6; i--){
            var data_img = result.rows[i].img;
            var data_name = result.rows[i].name;
            var data_content = result.rows[i].content;
            var data_text = result.rows[i].text;
            var data_price = result.rows[i].price;
            var data_cnt = result.rows[i].cnt1;
            Content1.push({data_img: data_img, data_name :data_name, data_content : data_content,data_price : data_price, data_text : data_text, data_cnt1 : data_cnt});
          }
        })
        .catch(err => {
          console.log(err);
        })
        var Content = {Content1};
        console.log(Content);
        return res.send(Content);
})
router.post('/imgSel', async (req, res, next) => {//머신러닝 요청
  console.log(`${process.pid}번 워커 실행`);  
  let kaka = req.body.result;
  let black ='';
  let result1 = false;
  let result2 = false;
  const mat=['닭가슴살','치즈','양파','피클','양상추','리코타치즈','발사믹 소스','토마토','치킨','마살라소스','버터', '마요네즈','계란','감자','불고기', '돼지고기','소고기','빵','통다리',
  '햄','새우','스리라차','베이컨','화이트치즈소스','할라피뇨']
  const menu = ['화이트 갈릭 버거','인크레더블 버거','딥치즈 버거','리샐 버거','불고기 포테이토 버거','휠렛 포테이토 버거','치즈 베이컨 버거','스파이시 불고기 버거', '스파이시 디럭스 불고기 버거','언블리버블 버거','통새우 버거','디럭스 불고기 버거','불싸이 버거','싸이버거','마살라 버거','할라피뇨 통가슴살 버거','쉬프림 포테이토 버거','살사리코 버거','할라피뇨 통살버거','휠렛 버거','햄치즈 휠렛 버거']
  let mat1 =[];
  let menu1 = [];
  let resu = 0;
  var Content1 = new Array();
  var favor = new Array();
  Content1.push({kaka:kaka});
  const path = require('path')
  const {spawn} = require('child_process')
  /**
     * Run python myscript, pass in `-u` to not buffer console output
     * @return {ChildProcess}
  */
  function runScript(){
    return spawn('python', [
        "-u",
        path.join(__dirname, 'test2.py'),
      "--foo", kaka,
    ]);
  }
  const subprocess = runScript()
  // print output of script
  subprocess.stdout.on('data', async(data) => {
    console.log("resutl2" + result2);
    if(resu > 0 && result2 == false){
      console.log("resutl2" + result2);
      console.log('resu=' + data.toString());
      console.log(data.toString().indexOf('2'));
      if(data.toString().indexOf('1') != -1){
          favor.push('1');
      }
      if(data.toString().indexOf('2') != -1){
          favor.push('2');
      }
      if(data.toString().indexOf('3')!= -1){
          favor.push('3');
      }
      if(data.toString().indexOf('4')!= -1){
          favor.push('4');
      }
      console.log('favor = ' + favor.length);
      for(var i = 0; i<favor.length; i++ ){
        if (favor[i] == '1'){
          black = black + '맵다';
        }
        else if(favor[i] == '2'){
          black = black + '달다';
        }
        else if(favor[i] == '3'){
          black = black + '상큼하다';
        }
        else if(favor[i] == '4'){
          black = black + '느끼하다';
        }
        else{
          black = '';
        }
      }
      
      if (black.indexOf('달다') != -1 ){
        var Content1 = new Array();
        const favor = '달다';
        //const result = await Image.findAll()
        if (result1 == true){
          const result = await Image.findAndCountAll({ where: {[Op.and]:[{ favor: {[Op.like]:"%" + favor + "%"} },{content: {[Op.like]:"%" + mat1[0] + "%"} }]}})
        .then(result => {
          for (var i = 0; i < result.count ; i++){
            var data_img = result.rows[i].img;
            var data_name = result.rows[i].name;
            var data_content = result.rows[i].content;
            var data_text = result.rows[i].text;
            var data_price = result.rows[i].price;
            var data_cnt = result.rows[i].cnt1;
            console.log('data='+ data_cnt);
            data_cnt = data_cnt + 1;
            console.log('data='+ data_cnt);
            Image.update({cnt1:data_cnt},{where:{name:data_name}});
            Content1.push({data_img: data_img, data_name :data_name, data_content : data_content,data_price : data_price, data_text : data_text, data_cnt1 : data_cnt});
          }
        })
        .catch(err => {
          console.log(err);
        })
        var Content = {Content1};
        console.log(Content);
        return res.send(Content);
      }else{
          
          const result = await Image.findAndCountAll({ where: { favor: {[Op.like]:"%" + favor + "%"} } })
          .then(result => {
            for (var i = 0; i < result.count ; i++){
              var data_img = result.rows[i].img;
              var data_name = result.rows[i].name;
              var data_content = result.rows[i].content;
              var data_text = result.rows[i].text;
              var data_price = result.rows[i].price;
              var data_cnt = result.rows[i].cnt1;
              console.log('data='+ data_cnt);
              data_cnt = data_cnt + 1;
              console.log('data='+ data_cnt);
              Image.update({cnt1:data_cnt},{where:{name:data_name}});
              Content1.push({data_img: data_img, data_name :data_name, data_content : data_content,data_price : data_price, data_text : data_text, data_cnt1 : data_cnt});
            }
          })
          .catch(err => {
            console.log(err);
          })
          var Content = {Content1};
          console.log(Content);
          return res.send(Content);
        }
      }
      else if(black.indexOf('맵다') != -1){

        var Content1 = new Array();
        const favor = '맵다';
        //const result = await Image.findAll()
        console.log(result1);
        if (result1 == true){
            const result = await Image.findAndCountAll({ where: {[Op.and]:[{ favor: {[Op.like]:"%" + favor + "%"} },{content: {[Op.like]:"%" + mat1[0] + "%"} }]}})
          .then(result => {
            for (var i = 0; i < result.count ; i++){
              var data_img = result.rows[i].img;
              var data_name = result.rows[i].name;
              var data_content = result.rows[i].content;
              var data_text = result.rows[i].text;
              var data_price = result.rows[i].price;
              var data_cnt = result.rows[i].cnt1;
              console.log('data='+ data_cnt);
              data_cnt = data_cnt + 1;
              console.log('data='+ data_cnt);
              Image.update({cnt1:data_cnt},{where:{name:data_name}});
              Content1.push({data_img: data_img, data_name :data_name, data_content : data_content,data_price : data_price, data_text : data_text, data_cnt1 : data_cnt});
            }
          })
          .catch(err => {
            console.log(err);
          })
          var Content = {Content1};
          console.log(Content);
          return res.send(Content);
        }else if(result1 == false){
          console.log('ok');
          const result = await Image.findAndCountAll({ where: { favor: {[Op.like]:"%" + favor + "%"} } })
          .then(result => {
            for (var i = 0; i < result.count ; i++){
              var data_img = result.rows[i].img;
              var data_name = result.rows[i].name;
              var data_content = result.rows[i].content;
              var data_text = result.rows[i].text;
              var data_price = result.rows[i].price;
              var data_cnt = result.rows[i].cnt1;
              console.log('data='+ data_cnt);
              data_cnt = data_cnt + 1;
              console.log('data='+ data_cnt);
              Image.update({cnt1:data_cnt},{where:{name:data_name}});
              Content1.push({data_img: data_img, data_name :data_name, data_content : data_content,data_price : data_price, data_text : data_text, data_cnt1 : data_cnt});
            }
          })
          .catch(err => {
            console.log(err);
          })
          var Content = {Content1};
          console.log(Content);
          return res.send(Content);
        }
      }
        else if(black.indexOf('상큼하다') != -1){
          var Content1 = new Array();
          const favor = '상큼하다';
          //const result = await Image.findAll()
          if (result1 == true){
            const result = await Image.findAndCountAll({ where: {[Op.and]:[{ favor: {[Op.like]:"%" + favor + "%"} },{content: {[Op.like]:"%" + mat1[0] + "%"} }]}})
          .then(result => {
            for (var i = 0; i < result.count ; i++){
              var data_img = result.rows[i].img;
              var data_name = result.rows[i].name;
              var data_content = result.rows[i].content;
              var data_text = result.rows[i].text;
              var data_price = result.rows[i].price;
              var data_cnt = result.rows[i].cnt1;
              console.log('data='+ data_cnt);
              data_cnt = data_cnt + 1;
              console.log('data='+ data_cnt);
              Image.update({cnt1:data_cnt},{where:{name:data_name}});
              Content1.push({data_img: data_img, data_name :data_name, data_content : data_content,data_price : data_price, data_text : data_text, data_cnt1 : data_cnt});
            }
          })
          .catch(err => {
            console.log(err);
          })
          var Content = {Content1};
          console.log(Content);
          return res.send(Content);
        }else{
          
          const result = await Image.findAndCountAll({ where: { favor: {[Op.like]:"%" + favor + "%"} } })
          .then(result => {
            for (var i = 0; i < result.count ; i++){
              var data_img = result.rows[i].img;
              var data_name = result.rows[i].name;
              var data_content = result.rows[i].content;
              var data_text = result.rows[i].text;
              var data_price = result.rows[i].price;
              var data_cnt = result.rows[i].cnt1;
              console.log('data='+ data_cnt);
              data_cnt = data_cnt + 1;
              console.log('data='+ data_cnt);
              Image.update({cnt1:data_cnt},{where:{name:data_name}});
              Content1.push({data_img: data_img, data_name :data_name, data_content : data_content,data_price : data_price, data_text : data_text, data_cnt1 : data_cnt});
            }
          })
          .catch(err => {
            console.log(err);
          })
          var Content = {Content1};
          console.log(Content);
          return res.send(Content);
        }
        }else if(black.indexOf('느끼하다') != -1){
          var Content1 = new Array();
          const favor = '느끼하다';
          //const result = await Image.findAll()
          if (result1 == true){
              const result = await Image.findAndCountAll({ where: {[Op.and]:[{ favor: {[Op.like]:"%" + favor + "%"} },{content: {[Op.like]:"%" + mat1[0] + "%"} }]}})
            .then(result => {
              for (var i = 0; i < result.count ; i++){
                var data_img = result.rows[i].img;
                var data_name = result.rows[i].name;
                var data_content = result.rows[i].content;
                var data_text = result.rows[i].text;
                var data_price = result.rows[i].price;
                var data_cnt = result.rows[i].cnt1;
                console.log('data='+ data_cnt);
                data_cnt = data_cnt + 1;
                console.log('data='+ data_cnt);
                Image.update({cnt1:data_cnt},{where:{name:data_name}});
                Content1.push({data_img: data_img, data_name :data_name, data_content : data_content,data_price : data_price, data_text : data_text, data_cnt1 : data_cnt});
              }
            })
            .catch(err => {
              console.log(err);
            })
            var Content = {Content1};
            console.log(Content);
            return res.send(Content);
          }else{
            
            const result = await Image.findAndCountAll({ where: { favor: {[Op.like]:"%" + favor + "%"} } })
            .then(result => {
              for (var i = 0; i < result.count ; i++){
                var data_img = result.rows[i].img;
                var data_name = result.rows[i].name;
                var data_content = result.rows[i].content;
                var data_text = result.rows[i].text;
                var data_price = result.rows[i].price;
                var data_cnt = result.rows[i].cnt1;
                console.log('data='+ data_cnt);
                data_cnt = data_cnt + 1;
                console.log('data='+ data_cnt);
                Image.update({cnt1:data_cnt},{where:{name:data_name}});
                Content1.push({data_img: data_img, data_name :data_name, data_content : data_content,data_price : data_price, data_text : data_text, data_cnt1 : data_cnt});
              }
            })
            .catch(err => {
              console.log(err);
            })
            var Content = {Content1};
            console.log(Content);
            return res.send(Content);
          }
      }else{
        var Content1 = new Array();
          const favor = '담백하다';
          //const result = await Image.findAll()
          if (result1 == true){
              const result = await Image.findAndCountAll({ where: {[Op.and]:[{ favor: {[Op.like]:"%" + favor + "%"} },{content: {[Op.like]:"%" + mat1[0] + "%"} }]}})
            .then(result => {
              for (var i = 0; i < result.count ; i++){
                var data_img = result.rows[i].img;
                var data_name = result.rows[i].name;
                var data_content = result.rows[i].content;
                var data_text = result.rows[i].text;
                var data_price = result.rows[i].price;
                var data_cnt = result.rows[i].cnt1;
                console.log('data='+ data_cnt);
                data_cnt = data_cnt + 1;
                console.log('data='+ data_cnt);
                Image.update({cnt1:data_cnt},{where:{name:data_name}});
                Content1.push({data_img: data_img, data_name :data_name, data_content : data_content,data_price : data_price, data_text : data_text, data_cnt1 : data_cnt});
              }
            })
            .catch(err => {
              console.log(err);
            })
            var Content = {Content1};
            console.log(Content);
            return res.send(Content);
          }else{
            
            const result = await Image.findAndCountAll({ where: { favor: {[Op.like]:"%" + favor + "%"} } })
            .then(result => {
              for (var i = 0; i < result.count ; i++){
                var data_img = result.rows[i].img;
                var data_name = result.rows[i].name;
                var data_content = result.rows[i].content;
                var data_text = result.rows[i].text;
                var data_price = result.rows[i].price;
                var data_cnt = result.rows[i].cnt1;
                console.log('data='+ data_cnt);
                data_cnt = data_cnt + 1;
                console.log('data='+ data_cnt);
                Image.update({cnt1:data_cnt},{where:{name:data_name}});
                Content1.push({data_img: data_img, data_name :data_name, data_content : data_content,data_price : data_price, data_text : data_text, data_cnt1 : data_cnt});
              }
            })
            .catch(err => {
              console.log(err);
            })
            var Content = {Content1};
            console.log(Content);
            return res.send(Content);
          }
      }
    }
    else{
      console.log('qweqwe');
      var Content1 = new Array();
      for(var j = 0; j <menu.length; j++ ){
        if (kaka.indexOf(menu[j]) != -1){
            menu1.push(menu[j]);
            console.log(menu1);
        }
      }
      console.log('메뉴' + menu1.length);
      if(menu1.length > 0){
        let name = menu1[0];
        const result = await Image.findAndCountAll({ where: { name: {[Op.like]:"%" + name + "%"} } })
            .then(result => {
              for (var i = 0; i < result.count ; i++){
                var data_img = result.rows[i].img;
                var data_name = result.rows[i].name;
                var data_content = result.rows[i].content;
                var data_text = result.rows[i].text;
                var data_price = result.rows[i].price;
                var data_cnt = result.rows[i].cnt1;
                console.log('data='+ data_cnt);
                data_cnt = data_cnt + 1;
                console.log('data='+ data_cnt);
                Image.update({cnt1:data_cnt},{where:{name:data_name}});
                Content1.push({data_img: data_img, data_name :data_name, data_content : data_content,data_price : data_price, data_text : data_text, data_cnt1 : data_cnt});
              }
            })
            .catch(err => {
              console.log(err);
            })
            var Content = {Content1};
            console.log(Content);
            result2 = true;
            console.log("1212" + result2)
            return res.send(Content);
      }else{
      for(var j = 0; j <mat.length; j++ ){
          if (kaka.indexOf(mat[j]) != -1){
              mat1.push(mat[j]);
              console.log(mat1);
          }
      }
      if(mat1.length > 0){
        result1 = true;
      }
    }
    }
    
    resu = resu + 1;
    
    console.log(resu);
  });
  subprocess.stderr.on('data', (data) => {
    console.log(`error:${data}`);
  });
  subprocess.stderr.on('close', () => {
    console.log("Closed");
  });
  

});
router.post('/imgAll', async (req, res, next) => {
  console.log(`${process.pid}번 워커 실행`);  
  console.log(typeof(req.body.result));
  const kaka = req.body.result;
  console.log(kaka);
  var Content1 = new Array();
  console.log(typeof(Content));
  //const result = await Image.findAndCountAll({ where: { favor: {[Op.like]:"%" + favor + "%"} } })
  const result = await Image.findAndCountAll()
  .then(result => {
    console.log(result.count);
    for (var i = 0; i < result.count ; i++){
      var data_img = result.rows[i].img;
      var data_name = result.rows[i].name;
      var data_content = result.rows[i].content;
      var data_text = result.rows[i].text;
      var data_price = result.rows[i].price;
      console.log(data_name);
      Content1.push({data_img: data_img, data_name :data_name, data_content : data_content,data_price : data_price, data_text : data_text });
    }
  })
  .catch(err => {
    console.log(err);
  })
  var Content = {Content1};
  console.log(Content);
  
return res.send(Content);
});
module.exports = router;