module.exports = (sequelize, DataTypes) => (
    sequelize.define('image', {
      cnt1:{
        type : DataTypes.INTEGER,
        allowNull:false,
      },
      content: {
        type: DataTypes.STRING(140),
        allowNull: false,
      },
      img: {
        type: DataTypes.STRING(200),
        allowNull: true,
      },
      name: {
        type: DataTypes.STRING(140),
        allowNull: false,
      },
      favor: {
        type: DataTypes.STRING(200),
        allowNull: true,
      },
      text: {
        type: DataTypes.STRING(200),
        allowNull: true,
      },
      price: {
        type: DataTypes.STRING(200),
        allowNull: true,
      },
      
      
    }, {
      timestamps: true,
      paranoid: true,
    })
  );